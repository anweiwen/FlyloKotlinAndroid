package com.flylo.flylopay;

/**
 * @ProjectName: RentHouse
 * @Package: com.flylo
 * @ClassName: PayResultListener
 * @Author: ANWEN
 * @CreateDate: 2020/7/11 4:55 PM
 * @UpdateUser:
 * @UpdateDate: 2020/7/11 4:55 PM
 * @UpdateRemark:
 * @Version:
 */
public interface PayResultListener {
    void paySuccess();
    void payError();
    void payCancel();
}
