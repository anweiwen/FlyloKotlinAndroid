package com.flylo.flylopay;


import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.flylo.flylopay.wechatpay.Constants;
import com.flylo.threebeads.alipay.PayResult;

import java.util.Map;

/**
 * @ProjectName: RentHouse
 * @Package: com.flylo
 * @ClassName: AliPay
 * @Author: ANWEN
 * @CreateDate: 2020/7/11 4:31 PM
 * @UpdateUser:
 * @UpdateDate: 2020/7/11 4:31 PM
 * @UpdateRemark:
 * @Version:
 */
public class AliPayTool {

    private Activity activity;

    public AliPayTool(Activity activity, PayResultListener listener) {
        this.activity = activity;
        Constants.listener = listener;
    }

    // 支付宝
    private final int SDK_PAY_FLAG = 1001;

    public void payAlipay(final String value) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(value, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    if (resultStatus.equals("9000")) {
                        // 支付成功
                        if (Constants.listener != null) {
                            Constants.listener.paySuccess();
                        }
                    } else if (resultStatus.equals("6001")) {
                        // 支付取消
                        if (Constants.listener != null) {
                            Constants.listener.payCancel();
                        }
                    } else if (resultStatus.equals("4000")) {
                        // 支付失败
                        if (Constants.listener != null) {
                            Constants.listener.payError();
                        }
                    }

                    Constants.listener = null;
                    break;
            }
        }
    };

    private void payResult(int i) {

    }
}
