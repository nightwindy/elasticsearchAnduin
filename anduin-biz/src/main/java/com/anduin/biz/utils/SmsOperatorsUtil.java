package com.anduin.biz.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.anduin.biz.enums.SmsOperatorsEnum;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;


/**
 * Created by yida on 16/1/20.
 */
public class SmsOperatorsUtil {

    private static final Logger LOG = LoggerFactory.getLogger(SmsOperatorsUtil.class);

    private static final String MHC_B2B = "【卖好车】";

    private static final int SUCC_CODE = 0;

    /**
     * luosimao 配置信息
     */
    private static String LSM_API = "key-ac2c1ea8eaa61e7ca617e17be7720b7f";

    private static String LSM_BASE_URL = "http://sms-api.luosimao.com/";

    private static String LSM_VERSION_NUM = "v1";

    private static String LSM_SEND_SMS_URL = LSM_BASE_URL + LSM_VERSION_NUM + "/send.json";

    /**
     * 云片 配置信息
     */
    private static String YP_SMS_API_KEY = "a74bd6857b1782a281154eeef5e7a11e";

    // 云片营销账号API-KEY
    private static String YP_YX_SMS_API_KEY = "ae84b3f5d70e781b8bc97cee4d508eac";

    private static String YP_BASE_URI = "http://yunpian.com/";

    private static String YP_VERSION = "v1";

    private static String YP_SMS_SEND_URL = YP_BASE_URI + YP_VERSION + "/sms/send.json";

    /**
     * 云片语音 配置信息
     */
    private static String YP_VOICE_SMS_API_KEY = "a74bd6857b1782a281154eeef5e7a11e";

    private static String YP_VOICE_BASE_URL = "http://voice.yunpian.com/";

    private static String YP_VOICE_VERSION_NUM = "v1";

    private static String YP_VOICE_SMS_SEND_URL = YP_VOICE_BASE_URL + YP_VOICE_VERSION_NUM + "/voice/send.json";


    /**
     * 发送短信接口
     *
     * @param content
     * @param mobile
     * @param currOperators
     * @return
     */
    public static BizResult<String> sendSms(String content, String mobile, String currOperators) {
        BizResult<String> bizResult = new BizResult<String>();

        BizResult verifyBizResult = verifySendSMSParam(content, mobile);
        if (!verifyBizResult.isSuccess()) {
            bizResult.setErrMsg(verifyBizResult.getErrMsg());
            return bizResult;
        }

        try {
            SmsOperatorsEnum operatorsEnum = SmsOperatorsEnum.LSM;
            if (StringUtils.isNotBlank(currOperators) &&
                    currOperators.equals(SmsOperatorsEnum.YUN_PIAN.code())) {
                operatorsEnum = SmsOperatorsEnum.YUN_PIAN;
            }

            String sendResultStr = "";
            if (SmsOperatorsEnum.LSM == operatorsEnum) {
                sendResultStr = SmsOperatorsUtil.sendLsmSmsForJson(content, mobile);
            }
            if (SmsOperatorsEnum.YUN_PIAN == operatorsEnum) {
                sendResultStr = SmsOperatorsUtil.sendYunPianSmsForJson(content, mobile);
            }

            BizResult sendBizResult = SmsOperatorsUtil.dealResult(operatorsEnum, sendResultStr);
            if (!sendBizResult.isSuccess()) {
                bizResult.setErrMsg(sendBizResult.getErrMsg());
            }
            return bizResult;
        } catch (Exception e) {
            LOG.error("发送短信失败,ERROR:", e);
            bizResult.setErrMsg("发送短信失败");
            return bizResult;
        }
    }


    /**
     * 统一的返回结果
     * <p/>
     * luosimao error:0 表示发送成功
     * yunpian code:0 表示发送成功
     *
     * @param operatorsEnum 发送方
     * @param result        返回值
     * @return 返回封装对象
     */
    public static BizResult<String> dealResult(SmsOperatorsEnum operatorsEnum, String result) {
        BizResult<String> bizResult = new BizResult<String>();
        if (null == operatorsEnum) {
            bizResult.setErrMsg("短信发送方为空!");
            return bizResult;
        }
        if (StringUtils.isBlank(result)) {
            bizResult.setErrMsg("发送方返回值为空");
            return bizResult;
        }
        LOG.info(String.format("短信发送方:%s, 返回值为:%s", operatorsEnum.code(), result));

        try {
            JSONObject jsonObj = JSON.parseObject(result);
            if (null == jsonObj) {
                bizResult.setErrMsg("发送方返回值为空");
                return bizResult;
            }

            if (jsonObj.getIntValue(getSuccCodeKey(operatorsEnum)) != SUCC_CODE) {
                LOG.error("发送短信失败，jsonObj" + jsonObj.toJSONString());
                bizResult.setErrMsg("信息发送失败");
            }

            return bizResult;
        } catch (Exception e) {
            LOG.info(String.format("短信发送方:%s, 返回值为:%s", operatorsEnum.code(), result), e);
            bizResult.setErrMsg("信息发送失败");
            return bizResult;
        }
    }

    private static String getSuccCodeKey(SmsOperatorsEnum operatorsEnum) {
        if (SmsOperatorsEnum.LSM == operatorsEnum) {
            return "error";
        }

        if (SmsOperatorsEnum.YUN_PIAN == operatorsEnum) {
            return "code";
        }
        return "";
    }


    /**
     * 螺丝帽发送返回json格式的短信
     *
     * @param text
     * @param mobile
     * @return
     */
    public static String sendLsmSmsForJson(String text, String mobile) {
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add("mobile", mobile);
        formData.add("message", text + MHC_B2B);

        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("api", LSM_API));

        WebResource webResource = client.resource(LSM_SEND_SMS_URL);
        ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);

        return response.getEntity(String.class);
    }

    /**
     * 云片发自动匹配模板短信
     *
     * @param text   　短信内容
     * @param mobile 　接受的手机号
     * @return json格式字符串
     * @throws Exception
     */
    public static String sendYunPianSmsForJson(String text, String mobile) {
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add("apikey", YP_SMS_API_KEY);
        formData.add("text", MHC_B2B + text);
        formData.add("mobile", mobile);

        Client client = Client.create();

        WebResource webResource = client.resource(YP_SMS_SEND_URL);
        ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);

        return response.getEntity(String.class);
    }

    /**
     * 发送语音短信接口
     *
     * @param code
     * @param mobile
     * @return
     */
    public static BizResult<String> sendVoiceSms(String code, String mobile) {
        BizResult<String> bizResult = verifySendSMSParam(code, mobile);
        if (!bizResult.isSuccess()) {
            return bizResult;
        }

        try {
            Client client = Client.create();
            WebResource webResource = client.resource(YP_VOICE_SMS_SEND_URL);
            MultivaluedMapImpl formData = new MultivaluedMapImpl();
            formData.add("apikey", YP_VOICE_SMS_API_KEY);
            formData.add("code", code);
            formData.add("mobile", mobile);
            formData.add("display_num", "4000390158");
            ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
            String textEntity = response.getEntity(String.class);

            BizResult<String> sendBizResult = dealResult(SmsOperatorsEnum.YUN_PIAN, textEntity);

            if (!sendBizResult.isSuccess()) {
                bizResult.setErrMsg(sendBizResult.getErrMsg());
            }
            return bizResult;
        } catch (Exception e) {
            LOG.error("发送语音短信失败,ERROR:", e);
            bizResult.setErrMsg("发送语音短信失败");
            return bizResult;
        }
    }

    public static BizResult<String> verifySendSMSParam(String code, String mobile) {
        BizResult<String> bizResult = new BizResult<String>();
        if (StringUtils.isBlank(mobile)) {
            bizResult.setErrMsg("手机号码为空");
            return bizResult;
        }
        if (!RegexUtil.match(mobile, RegexUtil.PHONE_REGEX)) {
            bizResult.setErrMsg("手机号码格式有误");
            return bizResult;
        }

        if (StringUtils.isBlank(code)) {
            bizResult.setErrMsg("信息发送内容为空");
            return bizResult;
        }
        return bizResult;
    }
}
