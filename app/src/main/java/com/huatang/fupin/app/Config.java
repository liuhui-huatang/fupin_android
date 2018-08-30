package com.huatang.fupin.app;

import com.huatang.fupin.R;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.SkinUtil;

import java.util.HashMap;
import java.util.Map;

public class Config {

    public static final String NAME = "name" ;
    public static final String TOKEN = "token";
    public static final String YEAR = "year";
    public static final String HEAD_PHOTO = "photo";
    public static final String PHONE = "phone";
    public static final String PASSWORD = "password";

    public static final String FCARD = "fcard";


    public static final String BAI_DU_MAP_DISTANCE = "2000";
    public static final boolean IS_OPEN_TEST_DISTANCE = false;

    //用户类型:1: 游客2: 贫困户3: 帮扶干部4: 管理员5: 村负责人
    public final static String Type = "type";

    public final static String YOUKU_TYPE = "1" ;
    public final static String PENKUNHU_TYPE = "2" ;
    public final static String GANBU_TYPE = "3" ;
    public final static String ADMIN_TYPE = "4" ;
    public final static String FUZEREN_TYPE = "5" ;

    public final static String bangfuriji = "帮扶日志";
    public final static String xiaoxitongzhi = "消息通知";
    public final static String gongshigonggao= "公示公告";
    public final static String fupenyaowen = "扶贫要闻";
    public final static String danganguanli = "档案管理";
    public final static String zuzhilingdao = "组织领导";
    public final static String qunzonghudong = "群众互动";
    public final static String dianxingyinlu ="典型引路";
    public final static String fupenxingdong = "扶贫行动";
    public final static String fupenzhengce = "扶贫政策";

    public final static String PENKUNHU_KEY = "poor";
    public final static String GANBU_KEY = "leader" ;
    public final static String ADMIN_KEY = "admin" ;
    public final static String FUZEREN_KEY = "fuzeren" ;
    public final static String YOUKE ="youke";
    public final static Map<String,Integer> menuMap = new HashMap<String,Integer>(){
        {
              put(bangfuriji, SkinUtil.getResouceId( R.mipmap.bangfuriji));
              put(xiaoxitongzhi,SkinUtil.getResouceId( R.mipmap.xiaoxitongzhi));
              put(gongshigonggao,SkinUtil.getResouceId( R.mipmap.gonggaogongshi));
              put(fupenyaowen,SkinUtil.getResouceId( R.mipmap.fupinxingdong));
              put(danganguanli,SkinUtil.getResouceId( R.mipmap.danganguanli));
              put(zuzhilingdao,SkinUtil.getResouceId( R.mipmap.zuzhilingdao));
              put(dianxingyinlu,SkinUtil.getResouceId( R.mipmap.dianxingyinlu));
              put(qunzonghudong,SkinUtil.getResouceId( R.mipmap.qunzonghudong));
              put(fupenxingdong,SkinUtil.getResouceId( R.mipmap.fupinxingdong));
              put(fupenzhengce,SkinUtil.getResouceId( R.mipmap.fupinzhengce));
        }
    };
    public final static String fupenyaowen_type = "1";
    public final static String gongshigonggao_type = "2";
    public final static String dianxingyinlu_type = "3";
    public final static String fupenxingdong_type = "4";
    public final static String qunzonghudong_type = "5";
    public final static String fupenzhengce_type = "6";
    public final static Map<String,String> typeMap = new HashMap<String,String >(){
        {
            put(fupenyaowen_type,fupenyaowen);
            put(gongshigonggao_type,gongshigonggao);
            put(dianxingyinlu_type,dianxingyinlu);
            put(fupenxingdong_type,fupenxingdong);
            put(qunzonghudong_type,qunzonghudong);

            put(fupenzhengce_type,fupenzhengce);

        }

    };

    //1扶贫要闻 2公示公告 / 三务公开 3典型引路 4扶贫行动 5群众活动
//0 致贫原因1 饮水方式2 房屋佐证图片3 资源资产佐证图片4运输车辆图片5 交通工具图片6 家用电器图片7 养殖图片

    public final static int DANGAN_POOR_CAUSE = 0;
    public final static int DANGAN_WATER = 1;
    public final static int DANGAN_HOUSE = 2;
    public final static int DANGAN_RESOUCE = 3;
    public final static int DANGAN_TRANSPORT = 4;
    public final static int DANGAN_VERICH = 5;
    public final static int DANGAN_ELECTOR = 6;
    public final static int DANGAN_BLEED = 7;

}
