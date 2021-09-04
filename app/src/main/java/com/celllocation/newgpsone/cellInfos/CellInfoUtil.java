package com.celllocation.newgpsone.cellInfos;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.text.TextUtils;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.celllocation.newgpsone.Position;
import com.celllocation.newgpsone.bean.GpsCorrect;

/**
 * Author:wang_sir
 * Time:2018/3/22 17:55
 * Description:This is CellInfoUtil
 */
public class CellInfoUtil {

    public static String URL_CELL_LOCATION = "http://218.246.35.74:5000";//基站定位接口

    private Context context;
    private static CellInfoUtil cellInfoUtil;

    public CellInfoUtil(Context context) {
        this.context = context.getApplicationContext();
    }

    public static CellInfoUtil getInstance(Context context) {

        if (cellInfoUtil == null) {
            synchronized (CellInfoUtil.class) {
                cellInfoUtil = new CellInfoUtil(context);
            }
        }
        return cellInfoUtil;
    }

    public Position GetJizhanPosition(final String lac, final String cid, final String nid, final String mnc) {

        final  Position position = new Position();
        CellPositionNetTask cellPositionNetTask = new CellPositionNetTask(new PositionCallBack() {
            @Override
            public void onSuccessed(CellPosition cellPosition) {
                String notice = cellPosition.getDesc();
                if (!"未查询到数据!".equals(notice)) {

                    Position p = resolveResponse( cellPosition, lac, cid, nid);
                    position.lac = p.lac;
                    position.cid = p.cid;
                    position.address = p.address;
                    position.nid = p.nid;
                    position.x = p.x;
                    position.y = p.y;

                }

            }
            @Override
            public void onErro() {
            }
        });
        cellPositionNetTask.getCellPosition(lac, cid, nid, mnc);
        return position;
    }


    /**
     * 解析数据
     *
     * @param cellPosition
     * @param lac
     * @param cid
     * @param nid
     * @return
     */
    public    Position resolveResponse(CellPosition cellPosition, String lac, String cid, String nid) {

        Position p = new Position();


        Object model = cellPosition.getModel();
        //  "注册码已经禁用"
        if (model.equals("注册码已经禁用")) {
            Toast.makeText(context, "注册码已经禁用,请联系管理员", Toast.LENGTH_SHORT).show();
            return p;
        } else if (model.equals("注册码次数已用完")) {
            Toast.makeText(context, "注册码次数已用完,请联系管理员", Toast.LENGTH_SHORT).show();
            return p;
        } else if (model.equals("注册码使用时间过期")) {
            Toast.makeText(context, "注册码使用时间过期,请联系管理员", Toast.LENGTH_SHORT).show();
            return p;
        } else {
            p.lac = Integer.parseInt(lac);
            p.cid = Integer.parseInt(cid);
            if (!TextUtils.isEmpty(nid)) {
                p.nid = Integer.parseInt(nid);
            }
            String lat = cellPosition.getModel().getLatitude();
            String lng = cellPosition.getModel().getLongitude();
            if (lat.isEmpty() || lng.isEmpty()) {
                p.x = 0.0;
                p.y = 0.0;
            } else {
//                LatLng mLatLng = RegOperateTool.GpsCorrectToLatLng(Double.parseDouble(lat), Double.parseDouble(lng));
//                p.x = mLatLng.latitude;
//                p.y = mLatLng.longitude;
                p.x = Double.parseDouble(lat);
                p.y = Double.parseDouble(lng);
            }
            p.address = cellPosition.getModel().getAddress();
            return p;
        }
    }


    public String  getMnc() {
        String mnc = "0";
        TelephonyManager mTManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (mTManager != null) {
            int phonetype = mTManager.getPhoneType();
            mnc = mTManager.getSubscriberId().substring(3, 5);
            //移动00、02、04、07
            //联通01、06、09
            //电信03、05、电信4G使用11

            if ("00".equals(mnc) || "02".equals(mnc) || "04".equals(mnc) || "07".equals(mnc)) {
                mnc = "0";
            } else if ("01".equals(mnc) || "06".equals(mnc) || "09".equals(mnc)) {
                mnc = "1";
            } else {
                mnc = "3";
            }
        }
        return mnc;

    }


    public String getNid(){
        String nid = "";
        TelephonyManager mTManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (mTManager != null) {
            int phonetype = mTManager.getPhoneType();
            if (phonetype == TelephonyManager.PHONE_TYPE_CDMA){
                CdmaCellLocation gcl = (CdmaCellLocation) mTManager
                        .getCellLocation();
                if (gcl != null) {
                    nid = String.valueOf(gcl.getNetworkId());
                }
            }
        }
        return nid;
    }
    /**
     * Gps纠偏
     *
     * @param lat gps中的纬度
     * @param lng gps中的经度
     * @return
     */
    public static LatLng GpsCorrectToLatLng(double lat, double lng) {
        double[] latlng = {lat, lng};
        GpsCorrect.transform(lat, lng, latlng);
        double lat_ = latlng[0];
        double lng_ = latlng[1];
        return new LatLng(lat_, lng_);
    }
}
