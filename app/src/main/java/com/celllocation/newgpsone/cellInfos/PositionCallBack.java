package com.celllocation.newgpsone.cellInfos;

import com.celllocation.newgpsone.bean.CellPosition;

/**
 * Author:wang_sir
 * Time:2018/3/21 14:02
 * Description:This is PositionCallBack
 */
public interface PositionCallBack {

   void onSuccessed(CellPosition position);
   void onErro();
}
