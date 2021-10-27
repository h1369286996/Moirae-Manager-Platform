-- ----------------------------
-- init t_project_temp
-- ----------------------------
TRUNCATE t_project_temp;
INSERT INTO `t_project_temp` VALUES ('1', '协同营销', '协同营销模板', '1', now(), now());

-- ----------------------------
-- init t_workflow_temp
-- ----------------------------
TRUNCATE t_workflow_temp;
INSERT INTO `t_workflow_temp` VALUES ('1', '1', '协同营销对应工作流', '协同营销对应工作流', '1', '0', '1', now(), now());

-- ----------------------------
-- init t_algorithm
-- ----------------------------
TRUNCATE t_algorithm;
INSERT INTO `t_algorithm` VALUES ('1', '逻辑回归训练', 'lr_train.py用于做逻辑回归训练', '春明提供支持', '3', '1', 'python', 'window,linux,mac', '3', '8', '2', '2', '2', '60000', '0', '1', '1', '1', '2021-10-25 15:16:02', '2021-10-27 16:56:53');
INSERT INTO `t_algorithm` VALUES ('2', '分类模型预测', 'lr_predict.py用于做逻辑回归预测', '春明提供支持', '3', '1', 'python', 'window,linux,mac', '3', '8', '2', '2', '2', '60000', '1', '1', '1', '1', '2021-10-25 15:16:02', '2021-10-27 16:56:54');
INSERT INTO `t_algorithm` VALUES ('3', '隐私集合求交（PSI）', '隐私集合求交（PSI）', '金广原型', '3', '2', 'python', 'window,linux,mac', '1', '8', '2', '2', '2', '60000', '0', '1', '1', '1', '2021-10-25 16:47:14', '2021-10-27 16:56:55');
INSERT INTO `t_algorithm` VALUES ('4', '隐私求和', '隐私求和', '金广原型', '3', '2', 'python', 'window,linux,mac', '1', '8', '2', '2', '2', '60000', '0', '1', '1', '1', '2021-10-25 17:01:40', '2021-10-27 16:56:56');
INSERT INTO `t_algorithm` VALUES ('5', '缺失值处理', '缺失值处理', '金广原型', '3', '2', 'python', 'window,linux,mac', '2', '8', '2', '2', '2', '60000', '0', '1', '1', '1', '2021-10-25 17:03:34', '2021-10-27 16:57:02');

-- ----------------------------
-- init t_algorithm_code
-- ----------------------------
TRUNCATE t_algorithm_code;
INSERT INTO `t_algorithm_code` VALUES ('1', '1', '2', '# coding:utf-8\n\nimport sys\nsys.path.append(\"..\")\nimport os\nimport math\nimport json\nimport time\nimport logging\nimport shutil\nimport numpy as np\nimport pandas as pd\nimport tensorflow as tf\nimport latticex.rosetta as rtt\nimport channel_sdk\n\n\nnp.set_printoptions(suppress=True)\ntf.compat.v1.logging.set_verbosity(tf.compat.v1.logging.ERROR)\nos.environ[\'TF_CPP_MIN_LOG_LEVEL\'] = \'2\'\nrtt.set_backend_loglevel(5)  # All(0), Trace(1), Debug(2), Info(3), Warn(4), Error(5), Fatal(6)\nlog = logging.getLogger(__name__)\n\nclass PrivacyLRTrain(object):\n    \'\'\'\n    Privacy logistic regression train base on rosetta.\n    \'\'\'\n\n    def __init__(self,\n                 channel_config: str,\n                 cfg_dict: dict,\n                 data_party: list,\n                 result_party: list,\n                 results_dir: str):\n        log.info(f\"channel_config:{channel_config}\")\n        log.info(f\"cfg_dict:{cfg_dict}\")\n        log.info(f\"data_party:{data_party}, result_party:{result_party}, results_dir:{results_dir}\")\n        assert isinstance(channel_config, str), \"type of channel_config must be str\"\n        assert isinstance(cfg_dict, dict), \"type of cfg_dict must be dict\"\n        assert isinstance(data_party, (list, tuple)), \"type of data_party must be list or tuple\"\n        assert isinstance(result_party, (list, tuple)), \"type of result_party must be list or tuple\"\n        assert isinstance(results_dir, str), \"type of results_dir must be str\"\n        \n        self.channel_config = channel_config\n        self.data_party = list(data_party)\n        self.result_party = list(result_party)\n        self.party_id = cfg_dict[\"party_id\"]\n        self.input_file = cfg_dict[\"data_party\"].get(\"input_file\")\n        self.key_column = cfg_dict[\"data_party\"].get(\"key_column\")\n        self.selected_columns = cfg_dict[\"data_party\"].get(\"selected_columns\")\n\n        dynamic_parameter = cfg_dict[\"dynamic_parameter\"]\n        self.label_owner = dynamic_parameter.get(\"label_owner\")\n        if self.party_id == self.label_owner:\n            self.label_column = dynamic_parameter.get(\"label_column\")\n            self.data_with_label = True\n        else:\n            self.label_column = \"\"\n            self.data_with_label = False\n                        \n        algorithm_parameter = dynamic_parameter[\"algorithm_parameter\"]\n        self.epochs = algorithm_parameter.get(\"epochs\", 10)\n        self.batch_size = algorithm_parameter.get(\"batch_size\", 256)\n        self.learning_rate = algorithm_parameter.get(\"learning_rate\", 0.001)\n        self.use_validation_set = algorithm_parameter.get(\"use_validation_set\", True)\n        self.validation_set_rate = algorithm_parameter.get(\"validation_set_rate\", 0.2)\n        self.predict_threshold = algorithm_parameter.get(\"predict_threshold\", 0.5)\n\n        self.output_file = os.path.join(results_dir, \"model\")\n        \n        self.check_parameters()\n\n    def check_parameters(self):\n        log.info(f\"check parameter start.\")        \n        assert self.epochs > 0, \"epochs must be greater 0\"\n        assert self.batch_size > 0, \"batch size must be greater 0\"\n        assert self.learning_rate > 0, \"learning rate must be greater 0\"\n        assert 0 < self.validation_set_rate < 1, \"validattion set rate must be between (0,1)\"\n        assert 0 <= self.predict_threshold <= 1, \"predict threshold must be between [0,1]\"\n        \n        if self.input_file:\n            self.input_file = self.input_file.strip()\n        if self.party_id in self.data_party:\n            if os.path.exists(self.input_file):\n                input_columns = pd.read_csv(self.input_file, nrows=0)\n                input_columns = list(input_columns.columns)\n                if self.key_column:\n                    assert self.key_column in input_columns, f\"key_column:{self.key_column} not in input_file\"\n                if self.selected_columns:\n                    error_col = []\n                    for col in self.selected_columns:\n                        if col not in input_columns:\n                            error_col.append(col)   \n                    assert not error_col, f\"selected_columns:{error_col} not in input_file\"\n                if self.label_column:\n                    assert self.label_column in input_columns, f\"label_column:{self.label_column} not in input_file\"\n            else:\n                raise Exception(f\"input_file is not exist. input_file={self.input_file}\")\n        log.info(f\"check parameter finish.\")\n                        \n        \n    def train(self):\n        \'\'\'\n        Logistic regression training algorithm implementation function\n        \'\'\'\n\n        log.info(\"extract feature or label.\")\n        train_x, train_y, val_x, val_y = self.extract_feature_or_label(with_label=self.data_with_label)\n        \n        log.info(\"start create and set channel.\")\n        self.create_set_channel()\n        log.info(\"waiting other party connect...\")\n        rtt.activate(\"SecureNN\")\n        log.info(\"protocol has been activated.\")\n        \n        log.info(f\"start set save model. save to party: {self.result_party}\")\n        rtt.set_saver_model(False, plain_model=self.result_party)\n        # sharing data\n        log.info(f\"start sharing train data. data_owner={self.data_party}, label_owner={self.label_owner}\")\n        shard_x, shard_y = rtt.PrivateDataset(data_owner=self.data_party, label_owner=self.label_owner).load_data(train_x, train_y, header=0)\n        log.info(\"finish sharing train data.\")\n        column_total_num = shard_x.shape[1]\n        log.info(f\"column_total_num = {column_total_num}.\")\n        \n        if self.use_validation_set:\n            log.info(\"start sharing validation data.\")\n            shard_x_val, shard_y_val = rtt.PrivateDataset(data_owner=self.data_party, label_owner=self.label_owner).load_data(val_x, val_y, header=0)\n            log.info(\"finish sharing validation data.\")\n\n        if self.party_id not in self.data_party:  \n            # mean the compute party and result party\n            log.info(\"compute start.\")\n            X = tf.placeholder(tf.float64, [None, column_total_num])\n            Y = tf.placeholder(tf.float64, [None, 1])\n            W = tf.Variable(tf.zeros([column_total_num, 1], dtype=tf.float64))\n            b = tf.Variable(tf.zeros([1], dtype=tf.float64))\n            logits = tf.matmul(X, W) + b\n            loss = tf.nn.sigmoid_cross_entropy_with_logits(labels=Y, logits=logits)\n            loss = tf.reduce_mean(loss)\n            # optimizer\n            optimizer = tf.train.GradientDescentOptimizer(self.learning_rate).minimize(loss)\n            init = tf.global_variables_initializer()\n            saver = tf.train.Saver(var_list=None, max_to_keep=5, name=\'v2\')\n            \n            pred_Y = tf.sigmoid(tf.matmul(X, W) + b)\n            reveal_Y = rtt.SecureReveal(pred_Y)\n            actual_Y = tf.placeholder(tf.float64, [None, 1])\n            reveal_Y_actual = rtt.SecureReveal(actual_Y)\n\n            with tf.Session() as sess:\n                log.info(\"session init.\")\n                sess.run(init)\n                # train\n                log.info(\"train start.\")\n                train_start_time = time.time()\n                batch_num = math.ceil(len(shard_x) / self.batch_size)\n                for e in range(self.epochs):\n                    for i in range(batch_num):\n                        bX = shard_x[(i * self.batch_size): (i + 1) * self.batch_size]\n                        bY = shard_y[(i * self.batch_size): (i + 1) * self.batch_size]\n                        sess.run(optimizer, feed_dict={X: bX, Y: bY})\n                        if (i % 50 == 0) or (i == batch_num - 1):\n                            log.info(f\"epoch:{e + 1}/{self.epochs}, batch:{i + 1}/{batch_num}\")\n                log.info(f\"model save to: {self.output_file}\")\n                saver.save(sess, self.output_file)\n                train_use_time = round(time.time()-train_start_time, 3)\n                log.info(f\"save model success. train_use_time={train_use_time}s\")\n                \n                if self.use_validation_set:\n                    Y_pred = sess.run(reveal_Y, feed_dict={X: shard_x_val})\n                    log.debug(f\"Y_pred:\\n {Y_pred[:10]}\")\n                    Y_actual = sess.run(reveal_Y_actual, feed_dict={actual_Y: shard_y_val})\n                    log.debug(f\"Y_actual:\\n {Y_actual[:10]}\")\n        \n            running_stats = str(rtt.get_perf_stats(True)).replace(\'\\n\', \'\').replace(\' \', \'\')\n            log.info(f\"running stats: {running_stats}\")\n        else:\n            log.info(\"computing, please waiting for compute finish...\")\n        rtt.deactivate()\n     \n        log.info(\"remove temp dir.\")\n        if self.party_id in (self.data_party + self.result_party):\n            self.remove_temp_dir()\n        else:\n            # delete the model in the compute party.\n            self.remove_output_dir()\n        \n        if (self.party_id in self.result_party) and self.use_validation_set:\n            log.info(\"result_party evaluate model.\")\n            from sklearn.metrics import roc_auc_score, roc_curve, f1_score, precision_score, recall_score, accuracy_score\n            Y_pred_prob = Y_pred.astype(\"float\").reshape([-1, ])\n            Y_true = Y_actual.astype(\"float\").reshape([-1, ])\n            auc_score = roc_auc_score(Y_true, Y_pred_prob)\n            Y_pred_class = (Y_pred_prob > self.predict_threshold).astype(\'int64\')  # default threshold=0.5\n            accuracy = accuracy_score(Y_true, Y_pred_class)\n            f1_score = f1_score(Y_true, Y_pred_class)\n            precision = precision_score(Y_true, Y_pred_class)\n            recall = recall_score(Y_true, Y_pred_class)\n            log.info(\"********************\")\n            log.info(f\"AUC: {round(auc_score, 6)}\")\n            log.info(f\"ACCURACY: {round(accuracy, 6)}\")\n            log.info(f\"F1_SCORE: {round(f1_score, 6)}\")\n            log.info(f\"PRECISION: {round(precision, 6)}\")\n            log.info(f\"RECALL: {round(recall, 6)}\")\n            log.info(\"********************\")\n        log.info(\"train finish.\")\n    \n    def create_set_channel(self):\n        \'\'\'\n        create and set channel.\n        \'\'\'\n        io_channel = channel_sdk.grpc.APIManager()\n        log.info(\"start create channel\")\n        channel = io_channel.create_channel(self.party_id, self.channel_config)\n        log.info(\"start set channel\")\n        rtt.set_channel(\"\", channel)\n        log.info(\"set channel success.\")\n    \n    def extract_feature_or_label(self, with_label: bool=False):\n        \'\'\'\n        Extract feature columns or label column from input file,\n        and then divide them into train set and validation set.\n        \'\'\'\n        train_x = \"\"\n        train_y = \"\"\n        val_x = \"\"\n        val_y = \"\"\n        temp_dir = self.get_temp_dir()\n        if self.party_id in self.data_party:\n            if self.input_file:\n                if with_label:\n                    usecols = self.selected_columns + [self.label_column]\n                else:\n                    usecols = self.selected_columns\n                \n                input_data = pd.read_csv(self.input_file, usecols=usecols, dtype=\"str\")\n                input_data = input_data[usecols]\n                # only if self.validation_set_rate==0, split_point==input_data.shape[0]\n                split_point = int(input_data.shape[0] * (1 - self.validation_set_rate))\n                assert split_point > 0, f\"train set is empty, because validation_set_rate:{self.validation_set_rate} is too big\"\n                \n                if with_label:\n                    y_data = input_data[self.label_column]\n                    train_y = os.path.join(temp_dir, f\"train_y_{self.party_id}.csv\")\n                    y_data.iloc[:split_point].to_csv(train_y, header=True, index=False)\n                    if self.use_validation_set:\n                        assert split_point < input_data.shape[0], \\\n                            f\"validation set is empty, because validation_set_rate:{self.validation_set_rate} is too small\"\n                        val_y = os.path.join(temp_dir, f\"val_y_{self.party_id}.csv\")\n                        y_data.iloc[split_point:].to_csv(val_y, header=True, index=False)\n                    del input_data[self.label_column]\n                \n                x_data = input_data\n                train_x = os.path.join(temp_dir, f\"train_x_{self.party_id}.csv\")\n                x_data.iloc[:split_point].to_csv(train_x, header=True, index=False)\n                if self.use_validation_set:\n                    assert split_point < input_data.shape[0], \\\n                            f\"validation set is empty, because validation_set_rate:{self.validation_set_rate} is too small\"\n                    val_x = os.path.join(temp_dir, f\"val_x_{self.party_id}.csv\")\n                    x_data.iloc[split_point:].to_csv(val_x, header=True, index=False)\n            else:\n                raise Exception(f\"data_node {self.party_id} not have data. input_file:{self.input_file}\")\n        return train_x, train_y, val_x, val_y\n    \n    def get_temp_dir(self):\n        \'\'\'\n        Get the directory for temporarily saving files\n        \'\'\'\n        temp_dir = os.path.join(os.path.dirname(self.output_file), \'temp\')\n        if not os.path.exists(temp_dir):\n            os.makedirs(temp_dir, exist_ok=True)\n        return temp_dir\n\n    def remove_temp_dir(self):\n        \'\'\'\n        Delete all files in the temporary directory, these files are some temporary data.\n        Only delete temp file.\n        \'\'\'\n        temp_dir = self.get_temp_dir()\n        if os.path.exists(temp_dir):\n            shutil.rmtree(temp_dir)\n    \n    def remove_output_dir(self):\n        \'\'\'\n        Delete all files in the temporary directory, these files are some temporary data.\n        This is used to delete all output files of the non-resulting party\n        \'\'\'\n        temp_dir = os.path.dirname(self.output_file)\n        if os.path.exists(temp_dir):\n            shutil.rmtree(temp_dir)\n\n\ndef main(channel_config: str, cfg_dict: dict, data_party: list, result_party: list, results_dir: str):\n    \'\'\'\n    This is the entrance to this module\n    \'\'\'\n    privacy_lr = PrivacyLRTrain(channel_config, cfg_dict, data_party, result_party, results_dir)\n    privacy_lr.train()\n', null, '1', '2021-10-12 15:56:27', '2021-10-26 10:26:48');
INSERT INTO `t_algorithm_code` VALUES ('2', '2', '2', '# coding:utf-8\n\nimport sys\nsys.path.append(\"..\")\nimport os\nimport math\nimport json\nimport time\nimport logging\nimport shutil\nimport numpy as np\nimport pandas as pd\nimport tensorflow as tf\nimport latticex.rosetta as rtt\nimport channel_sdk\n\n\nnp.set_printoptions(suppress=True)\ntf.compat.v1.logging.set_verbosity(tf.compat.v1.logging.ERROR)\nos.environ[\'TF_CPP_MIN_LOG_LEVEL\'] = \'2\'\nrtt.set_backend_loglevel(5)  # All(0), Trace(1), Debug(2), Info(3), Warn(4), Error(5), Fatal(6)\nlog = logging.getLogger(__name__)\n\nclass PrivacyLRPredict(object):\n    \'\'\'\n    Privacy logistic regression predict base on rosetta.\n    \'\'\'\n\n    def __init__(self,\n                 channel_config: str,\n                 cfg_dict: dict,\n                 data_party: list,\n                 result_party: list,\n                 results_dir: str):\n        log.info(f\"channel_config:{channel_config}\")\n        log.info(f\"cfg_dict:{cfg_dict}\")\n        log.info(f\"data_party:{data_party}, result_party:{result_party}, results_dir:{results_dir}\")\n        assert isinstance(channel_config, str), \"type of channel_config must be str\"\n        assert isinstance(cfg_dict, dict), \"type of cfg_dict must be dict\"\n        assert isinstance(data_party, (list, tuple)), \"type of data_party must be list or tuple\"\n        assert isinstance(result_party, (list, tuple)), \"type of result_party must be list or tuple\"\n        assert isinstance(results_dir, str), \"type of results_dir must be str\"\n        \n        self.channel_config = channel_config\n        self.data_party = list(data_party)\n        self.result_party = list(result_party)\n        self.party_id = cfg_dict[\"party_id\"]\n        self.input_file = cfg_dict[\"data_party\"].get(\"input_file\")\n        self.key_column = cfg_dict[\"data_party\"].get(\"key_column\")\n        self.selected_columns = cfg_dict[\"data_party\"].get(\"selected_columns\")\n        dynamic_parameter = cfg_dict[\"dynamic_parameter\"]\n        self.model_restore_party = dynamic_parameter.get(\"model_restore_party\")\n        self.model_path = dynamic_parameter.get(\"model_path\")\n        self.model_file = os.path.join(self.model_path, \"model\")\n        self.predict_threshold = dynamic_parameter.get(\"predict_threshold\", 0.5)        \n        self.output_file = os.path.join(results_dir, \"result\")\n        self.data_party.remove(self.model_restore_party)  # except restore party\n        self.check_parameters()\n\n    def check_parameters(self):\n        log.info(f\"check parameter start.\")        \n        assert 0 <= self.predict_threshold <= 1, \"predict threshold must be between [0,1]\"\n        \n        if self.input_file:\n            self.input_file = self.input_file.strip()\n        if self.party_id in self.data_party:\n            if os.path.exists(self.input_file):\n                input_columns = pd.read_csv(self.input_file, nrows=0)\n                input_columns = list(input_columns.columns)\n                if self.key_column:\n                    assert self.key_column in input_columns, f\"key_column:{self.key_column} not in input_file\"\n                if self.selected_columns:\n                    error_col = []\n                    for col in self.selected_columns:\n                        if col not in input_columns:\n                            error_col.append(col)   \n                    assert not error_col, f\"selected_columns:{error_col} not in input_file\"\n            else:\n                raise Exception(f\"input_file is not exist. input_file={self.input_file}\")\n        if self.party_id == self.model_restore_party:\n            assert os.path.exists(self.model_path), f\"model path not found. model_path={self.model_path}\"\n        log.info(f\"check parameter finish.\")\n       \n\n    def predict(self):\n        \'\'\'\n        Logistic regression predict algorithm implementation function\n        \'\'\'\n\n        log.info(\"extract feature or id.\")\n        file_x, id_col = self.extract_feature_or_index()\n        \n        log.info(\"start create and set channel.\")\n        self.create_set_channel()\n        log.info(\"waiting other party connect...\")\n        rtt.activate(\"SecureNN\")\n        log.info(\"protocol has been activated.\")\n        \n        log.info(f\"start set restore model. restore party={self.model_restore_party}\")\n        rtt.set_restore_model(False, plain_model=self.model_restore_party)\n        # sharing data\n        log.info(f\"start sharing data. data_owner={self.data_party}\")\n        shard_x = rtt.PrivateDataset(data_owner=self.data_party).load_X(file_x, header=0)\n        log.info(\"finish sharing data .\")\n        column_total_num = shard_x.shape[1]\n        log.info(f\"column_total_num = {column_total_num}.\")\n\n        X = tf.placeholder(tf.float64, [None, column_total_num])\n        W = tf.Variable(tf.zeros([column_total_num, 1], dtype=tf.float64))\n        b = tf.Variable(tf.zeros([1], dtype=tf.float64))\n        saver = tf.train.Saver(var_list=None, max_to_keep=5, name=\'v2\')\n        init = tf.global_variables_initializer()\n        # predict\n        pred_Y = tf.sigmoid(tf.matmul(X, W) + b)\n        reveal_Y = rtt.SecureReveal(pred_Y)  # only reveal to result party\n\n        with tf.Session() as sess:\n            log.info(\"session init.\")\n            sess.run(init)\n            log.info(\"start restore model.\")\n            if self.party_id == self.model_restore_party:\n                if os.path.exists(os.path.join(self.model_path, \"checkpoint\")):\n                    log.info(f\"model restore from: {self.model_file}.\")\n                    saver.restore(sess, self.model_file)\n                else:\n                    raise Exception(\"model not found or model damaged\")\n            else:\n                log.info(\"restore model...\")\n                temp_file = os.path.join(self.get_temp_dir(), \'ckpt_temp_file\')\n                with open(temp_file, \"w\") as f:\n                    pass\n                saver.restore(sess, temp_file)\n            log.info(\"finish restore model.\")\n            \n            # predict\n            log.info(\"predict start.\")\n            predict_start_time = time.time()\n            Y_pred_prob = sess.run(reveal_Y, feed_dict={X: shard_x})\n            log.debug(f\"Y_pred_prob:\\n {Y_pred_prob[:10]}\")\n            predict_use_time = round(time.time() - predict_start_time, 3)\n            log.info(f\"predict success. predict_use_time={predict_use_time}s\")\n        rtt.deactivate()\n        log.info(\"rtt deactivate finish.\")\n        \n        if self.party_id in self.result_party:\n            log.info(\"predict result write to file.\")\n            output_file_predict_prob = os.path.splitext(self.output_file)[0] + \"_predict.csv\"\n            Y_pred_prob = Y_pred_prob.astype(\"float\")\n            Y_prob = pd.DataFrame(Y_pred_prob, columns=[\"Y_prob\"])\n            Y_class = (Y_pred_prob > self.predict_threshold) * 1\n            Y_class = pd.DataFrame(Y_class, columns=[\"Y_class\"])\n            Y_result = pd.concat([Y_prob, Y_class], axis=1)\n            Y_result.to_csv(output_file_predict_prob, header=True, index=False)\n        log.info(\"start remove temp dir.\")\n        self.remove_temp_dir()\n        log.info(\"predict finish.\")\n\n    def create_set_channel(self):\n        \'\'\'\n        create and set channel.\n        \'\'\'\n        io_channel = channel_sdk.grpc.APIManager()\n        log.info(\"start create channel\")\n        channel = io_channel.create_channel(self.party_id, self.channel_config)\n        log.info(\"start set channel\")\n        rtt.set_channel(\"\", channel)\n        log.info(\"set channel success.\")\n        \n    def extract_feature_or_index(self):\n        \'\'\'\n        Extract feature columns or index column from input file.\n        \'\'\'\n        file_x = \"\"\n        id_col = None\n        temp_dir = self.get_temp_dir()\n        if self.party_id in self.data_party:\n            if self.input_file:\n                usecols = [self.key_column] + self.selected_columns\n                input_data = pd.read_csv(self.input_file, usecols=usecols, dtype=\"str\")\n                input_data = input_data[usecols]\n                id_col = input_data[self.key_column]\n                file_x = os.path.join(temp_dir, f\"file_x_{self.party_id}.csv\")\n                x_data = input_data.drop(labels=self.key_column, axis=1)\n                x_data.to_csv(file_x, header=True, index=False)\n            else:\n                raise Exception(f\"data_party:{self.party_id} not have data. input_file:{self.input_file}\")\n        return file_x, id_col\n    \n    def get_temp_dir(self):\n        \'\'\'\n        Get the directory for temporarily saving files\n        \'\'\'\n        temp_dir = os.path.join(os.path.dirname(self.output_file), \'temp\')\n        if not os.path.exists(temp_dir):\n            os.makedirs(temp_dir, exist_ok=True)\n        return temp_dir\n\n    def remove_temp_dir(self):\n        \'\'\'\n        Delete all files in the temporary directory, these files are some temporary data.\n        Only delete temp file.\n        \'\'\'\n        temp_dir = self.get_temp_dir()\n        if os.path.exists(temp_dir):\n            shutil.rmtree(temp_dir)\n\n\ndef main(channel_config: str, cfg_dict: dict, data_party: list, result_party: list, results_dir: str):\n    \'\'\'\n    This is the entrance to this module\n    \'\'\'\n    privacy_lr = PrivacyLRPredict(channel_config, cfg_dict, data_party, result_party, results_dir)\n    privacy_lr.predict()\n', null, '1', '2021-10-12 15:56:27', '2021-10-26 10:26:51');
INSERT INTO `t_algorithm_code` VALUES ('3', '3', '2', '', null, '1', '2021-10-25 16:49:33', '2021-10-26 10:23:52');
INSERT INTO `t_algorithm_code` VALUES ('4', '4', '2', '', null, '1', '2021-10-25 17:04:39', '2021-10-26 10:23:52');
INSERT INTO `t_algorithm_code` VALUES ('5', '5', '2', '', null, '1', '2021-10-25 17:04:57', '2021-10-26 10:23:52');

-- ----------------------------
-- init t_workflow_node_temp
-- ----------------------------
TRUNCATE t_workflow_node_temp;
INSERT INTO `t_workflow_node_temp` VALUES ('1', '1', '1', '逻辑回归训练', '1', '2', '0', '1', now(), now());
INSERT INTO `t_workflow_node_temp` VALUES ('2', '1', '2', '分类模型预测', '2', null, '0', '1', now(), now());

-- ----------------------------
-- init t_algorithm_variable_struct
-- ----------------------------
TRUNCATE t_algorithm_variable_struct;
INSERT INTO `t_algorithm_variable_struct` VALUES ('1', '1', '{\"label_owner\":\"p0\",\"label_column\":\"Y\",\"algorithm_parameter\":{\"epochs\":10,\"batch_size\":256,\"learning_rate\":0.1,\"use_validation_set\":true,\"validation_set_rate\":0.2,\"predict_threshold\":0.5}}', '逻辑回归训练变量参数', '1', '2021-10-26 14:05:13', '2021-10-26 17:08:32');
INSERT INTO `t_algorithm_variable_struct` VALUES ('2', '2', '{\"model_restore_party\":\"p0\",\"model_path\":\"file_path\",\"predict_threshold\":0.5}', '逻辑回归预测变量参数', '1', '2021-10-26 14:05:13', '2021-10-26 14:05:13');
-- ----------------------------
-- init t_algorithm_type
-- ----------------------------
INSERT INTO `t_algorithm_type` VALUES ('1', '统计分析', '统计分析', '1', '1', now(), now());
INSERT INTO `t_algorithm_type` VALUES ('2', '特征工程', '特征工程', '2', '1', now(), now());
INSERT INTO `t_algorithm_type` VALUES ('3', '机器学习', '机器学习', '3', '1', now(), now());
