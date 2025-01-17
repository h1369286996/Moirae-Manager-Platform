package com.moirae.rosettaflow.initdata;

import cn.hutool.core.util.StrUtil;
import com.moirae.rosettaflow.common.constants.SysConfig;
import com.moirae.rosettaflow.common.utils.IoUtils;
import com.moirae.rosettaflow.mapper.domain.Algorithm;
import com.moirae.rosettaflow.mapper.domain.AlgorithmCode;
import com.moirae.rosettaflow.mapper.domain.AlgorithmVariable;
import com.moirae.rosettaflow.service.IAlgorithmCodeService;
import com.moirae.rosettaflow.service.IAlgorithmService;
import com.moirae.rosettaflow.service.IAlgorithmVariableService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/9/7
 * @description 初始化算法数据(数据必须按csv模版格式进行配置 ， 且字符编码为UTF - 8)
 */
@Slf4j
@Component
public class InitAlgorithm {

    @Resource
    private SysConfig sysConfig;

    @Resource
    private IAlgorithmService algorithmService;

    @Resource
    private IAlgorithmCodeService algorithmCodeService;

    @Resource
    private IAlgorithmVariableService algorithmVariableService;

//    @PostConstruct
    @Lock(keys = "InitAlgorithm")
    public void init() {
        //清除旧数据
        algorithmService.truncate();
        algorithmCodeService.truncate();
        algorithmVariableService.truncate();

        try {
            InputStream inputStream = IoUtils.readFileToInputStream(sysConfig.getAlgorithmFilepath());
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);
            String data;
            //过滤标题
            br.readLine();
            while ((data = br.readLine()) != null) {
                log.info("读取算法数据:{}", data);

                List<String> row = StrUtil.split(data, "|");

                Algorithm algorithm;
                AlgorithmCode algorithmCode;

                //遍历行
                algorithm = new Algorithm();
                algorithmCode = new AlgorithmCode();
                algorithm.setAlgorithmName(row.get(0));
                algorithm.setAlgorithmDesc(row.get(1));
                algorithm.setAuthor(row.get(2));
                algorithm.setMaxNumbers(Long.valueOf(row.get(3)));
                algorithm.setMinNumbers(Long.valueOf(row.get(4)));
                algorithm.setSupportLanguage(row.get(5));
                algorithm.setSupportOsSystem(row.get(6));
                algorithm.setAlgorithmType(Byte.valueOf(row.get(7)));
                algorithm.setCostMem(Long.valueOf(row.get(8)));
                algorithm.setCostCpu(Integer.valueOf(row.get(9)));
                algorithm.setCostGpu(Integer.valueOf(row.get(10)));
                algorithm.setCostBandwidth(Long.valueOf(row.get(11)));
                algorithm.setRunTime(Long.valueOf(row.get(12)));
                algorithm.setPublicFlag(Byte.valueOf(row.get(13)));
                algorithmService.save(algorithm);

                algorithmCode.setAlgorithmId(algorithm.getId());
                algorithmCode.setEditType(Byte.valueOf(row.get(14)));
                algorithmCode.setCalculateContractCode(row.get(15));
                algorithmCode.setDataSplitContractCode(row.get(16));
                algorithmCodeService.save(algorithmCode);

                String variableStr = row.get(17);
                AlgorithmVariable algorithmVariable;
                if (!StrUtil.hasBlank(variableStr)) {
                    //key1:value1;1;自变量1||key2:value2;2;自变量2
                    List<String> variables = StrUtil.split(variableStr, "#");
                    for (String variable : variables) {
                        List<String> values = StrUtil.split(variable, ";");
                        algorithmVariable = new AlgorithmVariable();
                        algorithmVariable.setAlgorithmId(algorithm.getId());
                        algorithmVariable.setVarKey(values.get(0));
                        algorithmVariable.setVarValue(values.get(1));
                        algorithmVariable.setVarType(Byte.valueOf(values.get(2)));
                        algorithmVariable.setVarDesc(values.get(3));
                        algorithmVariableService.save(algorithmVariable);
                    }
                }
            }

            br.close();
            isr.close();

        } catch (Exception e) {
            log.error("read init Algorithm csv file fail,fail reason is:{}", e.getMessage(), e);
        }
    }
}
