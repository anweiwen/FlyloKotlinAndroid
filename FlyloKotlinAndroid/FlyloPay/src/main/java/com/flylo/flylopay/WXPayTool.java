package com.flylo.flylopay;

import android.content.Context;
import android.widget.Toast;

import com.flylo.flylopay.bean.WXPayBean;
import com.flylo.flylopay.wechatpay.Constants;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * @ProjectName: RentHouse
 * @Package: com.flylo
 * @ClassName: WXPayTool
 * @Author: ANWEN
 * @CreateDate: 2020/7/11 4:20 PM
 * @UpdateUser:
 * @UpdateDate: 2020/7/11 4:20 PM
 * @UpdateRemark:
 * @Version:
 */
public class WXPayTool {

    private Context context;
    public WXPayTool(Context context, PayResultListener listener){
        this.context = context;
        Constants.listener = listener;
        initWx();
    }

    private IWXAPI wxApi;
    private void initWx(){
        wxApi = WXAPIFactory.createWXAPI(context, null);
        wxApi.registerApp(Constants.app_id);
    }

    // 微信
    public void weChatPay(WXPayBean wxPayBean) {
        if (!wxApi.isWXAppInstalled()) {
            showToast("请先安装微信");
            return;
        }
        if (wxPayBean == null) {
            return;
        }
        PayReq req = new PayReq();
        req.appId = wxPayBean.appid;
        req.partnerId = wxPayBean.partnerid;
        req.prepayId = wxPayBean.prepayid;
        req.nonceStr = wxPayBean.noncestr;
        req.timeStamp = wxPayBean.timestamp;
        req.packageValue = "Sign=WXPay";
        req.sign = wxPayBean.sign;
        wxApi.sendReq(req);
    }

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
