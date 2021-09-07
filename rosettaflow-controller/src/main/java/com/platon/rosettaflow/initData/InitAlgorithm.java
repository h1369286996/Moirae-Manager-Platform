//package com.platon.rosettaflow.initData;
//
//import cn.hutool.core.io.FileUtil;
//import cn.hutool.core.text.csv.CsvData;
//import cn.hutool.core.text.csv.CsvReader;
//import cn.hutool.core.text.csv.CsvRow;
//import cn.hutool.core.text.csv.CsvUtil;
//import cn.hutool.core.util.StrUtil;
//import com.platon.rosettaflow.common.constants.SysConfig;
//import com.platon.rosettaflow.mapper.domain.Algorithm;
//import com.platon.rosettaflow.mapper.domain.AlgorithmCode;
//import com.platon.rosettaflow.mapper.domain.AlgorithmVariable;
//import com.platon.rosettaflow.service.IAlgorithmCodeService;
//import com.platon.rosettaflow.service.IAlgorithmService;
//import com.platon.rosettaflow.service.IAlgorithmVariableService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//import java.util.List;
//
///**
// * @author hudenian
// * @date 2021/9/7
// * @description 初始化算法数据(数据必须按csv模版格式进行配置，且字符编码为UTF-8)
// */
//@Slf4j
//@Component
//public class InitAlgorithm {
//
//    @Resource
//    private SysConfig sysConfig;
//
//    @Resource
//    private IAlgorithmService algorithmService;
//
//    @Resource
//    private IAlgorithmCodeService algorithmCodeService;
//
//    @Resource
//    private IAlgorithmVariableService algorithmVariableService;
//
//    @PostConstruct
//    public void init() {
//        if (!sysConfig.isMasterNode()) {
//            return;
//        }
//        //清除旧数据
//        algorithmService.truncate();
//        algorithmCodeService.truncate();
//
//        CsvReader reader = CsvUtil.getReader();
//        //从文件中读取CSV数据
//        CsvData data = reader.read(FileUtil.file(sysConfig.getAlgorithmFilepath()));
//        List<CsvRow> rows = data.getRows();
//
//        Algorithm algorithm;
//        AlgorithmCode algorithmCode;
//
//        //遍历行
//        for (int i = 1; i < rows.size(); i++) {
//            //getRawList返回一个List列表，列表的每一项为CSV中的一个单元格（既逗号分隔部分）
//            List<String> row = rows.get(i).getRawList();
//            algorithm = new Algorithm();
//            algorithmCode = new AlgorithmCode();
//            algorithm.setAlgorithmName(row.get(0));
//            algorithm.setAlgorithmDesc(row.get(1));
//            algorithm.setAuthor(row.get(2));
//            algorithm.setMaxNumbers(Long.valueOf(row.get(3)));
//            algorithm.setMinNumbers(Long.valueOf(row.get(4)));
//            algorithm.setSupportLanguage(row.get(5));
//            algorithm.setSupportOsSystem(row.get(6));
//            algorithm.setAlgorithmType(Byte.valueOf(row.get(7)));
//            algorithm.setCostMem(Long.valueOf(row.get(8)));
//            algorithm.setCostCpu(Long.valueOf(row.get(9)));
//            algorithm.setCostGpu(Integer.valueOf(row.get(10)));
//            algorithm.setCostBandwidth(Long.valueOf(row.get(11)));
//            algorithm.setRunTime(Long.valueOf(row.get(12)));
//            algorithm.setPublicFlag(Byte.valueOf(row.get(13)));
//            algorithmService.save(algorithm);
//
//            algorithmCode.setAlgorithmId(algorithm.getId());
//            algorithmCode.setEditType(Byte.valueOf(row.get(14)));
//            algorithmCode.setCalculateContractCode(row.get(15));
//            algorithmCode.setDataSplitContractCode(row.get(16));
//            algorithmCodeService.save(algorithmCode);
//
//            String variableStr = row.get(17);
//            AlgorithmVariable algorithmVariable;
//            if (!StrUtil.hasBlank(variableStr)) {
//                //key1:value1;1;自变量1||key2:value2;2;自变量2
//                List<String> variables = StrUtil.split(variableStr, "||");
//                for (String variable : variables) {
//                    List<String> values = StrUtil.split(variable, ";");
//                    algorithmVariable = new AlgorithmVariable();
//                    algorithmVariable.setAlgorithmId(algorithm.getId());
//                    algorithmVariable.setVarKey(values.get(0));
//                    algorithmVariable.setVarValue(values.get(1));
//                    algorithmVariable.setVarType(Byte.valueOf(values.get(2)));
//                    algorithmVariable.setVarDesc(values.get(3));
//                    algorithmVariableService.save(algorithmVariable);
//                }
//            }
//        }
//    }
//}