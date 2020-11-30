package com.flylo.threebeads.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.flylo.flylopay.wechatpay.Constants;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	api = WXAPIFactory.createWXAPI(this, Constants.app_id);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		String result = null;
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			switch (resp.errCode) {
				case BaseResp.ErrCode.ERR_OK:
					result = "支付成功";
                    if (Constants.listener != null){
                        Constants.listener.paySuccess();
                    }
					break;
				case BaseResp.ErrCode.ERR_COMM:
					result = "支付失败";
                    if (Constants.listener != null){
                        Constants.listener.payError();
                    }
					break;
				case BaseResp.ErrCode.ERR_USER_CANCEL:
					result = "支付取消";
                    if (Constants.listener != null){
                        Constants.listener.payCancel();
                    }
					break;
			}
		}
        Constants.listener = null;
		finish();
	}
}