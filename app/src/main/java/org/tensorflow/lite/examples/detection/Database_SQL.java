package org.tensorflow.lite.examples.detection;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Hashtable;

public class Database_SQL {

    /**
     * TAG for debugging
     */
    public static final String TAG = "ShopDatabase";

    /**
     * Singleton instance
     */
    private static Database_SQL database;


    /**
     * database name
     */
    public static String DATABASE_NAME = "favorite.db";

    /**
     * table name for BOOK_INFO
     */
    public static String TABLE_SHOP_INFO = "shop_table";

    public static String TABLE_SHOP_BASKET = "basket_table";

    /**
     * version
     */
    public static int DATABASE_VERSION = 1;


    /**
     * Helper class defined
     */
    private DatabaseHelper dbHelper;

    /**
     * Database object
     */
    private SQLiteDatabase db;


    private Context context;

    /**
     * Constructor
     */
    private Database_SQL(Context context) {
        this.context = context;
    }


    public static Database_SQL getInstance(Context context) {
        if (database == null) {
            database = new Database_SQL(context);
        }

        return database;
    }

    /**
     * open database
     *
     * @return
     */
    public boolean open() { //데이터베이스 열기
        println("opening database [" + DATABASE_NAME + "].");

        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();

        return true;
    }

    /**
     * close database
     */
    public void close() {
        println("closing database [" + DATABASE_NAME + "].");
        db.close();
        database = null;
    }

    /**
     * execute raw query using the input SQL
     * close the cursor after fetching any result
     *
     * @param SQL
     * @return
     */
    public Cursor rawQuery(String SQL) {
        println("\nexecuteQuery called.\n");

        Cursor c1 = null;
        try {
            c1 = db.rawQuery(SQL, null);
            println("cursor count : " + c1.getCount());
        } catch(Exception ex) {
            Log.e(TAG, "Exception in executeQuery", ex);
        }

        return c1;
    }

    public boolean execSQL(String SQL) {
        println("\nexecute called.\n");

        try {
            Log.d(TAG, "SQL : " + SQL);
            db.execSQL(SQL);
        } catch(Exception ex) {
            Log.e(TAG, "Exception in executeQuery", ex);
            return false;
        }

        return true;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase _db) {

            //즐겨찾기 테이블
            println("creating table [" + TABLE_SHOP_INFO + "].");

            String DROP_SQL = "drop table if exists " + TABLE_SHOP_INFO;
            try {
                _db.execSQL(DROP_SQL);
            } catch(Exception ex) {
                Log.e(TAG, "Exception in DROP_SQL", ex);
            }

            String CREATE_SQL = "create table if not exists " + TABLE_SHOP_INFO + "("
                    + "  _id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + "  NAME TEXT, "
                    + "  CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP "
                    + ")";
            try {
                _db.execSQL(CREATE_SQL);
            } catch(Exception ex) {
                Log.e(TAG, "Exception in CREATE_SQL", ex);
            }

        }

        public void onOpen(SQLiteDatabase db) {
            println("opened database [" + DATABASE_NAME + "].");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            println("Upgrading database from version " + oldVersion + " to " + newVersion + ".");

            if (oldVersion < 2) {

            }
        }
    }

    public void insertRecord(String name) { //외부에서 사용할 때 실행되는 메소드(public)
        try {
            String sql = "insert into " + TABLE_SHOP_INFO + "(NAME) values(?)";

            Object[] params = {name};
            db.execSQL(sql, params);
//            Toast.makeText(context.getApplicationContext(), name + " 데이터 잘 들어감", Toast.LENGTH_SHORT).show();
        } catch(Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, "Exception in executing insert SQL.", ex);
        }
    }

    public void updateRecord(String name, String price, String info) { //외부에서 사용할 때 실행되는 메소드(public)
        name = "\""+name +"\"";
        price = "\""+price +"\"";
        info = "\""+info +"\"";
        try {
            String sql = "UPDATE " + TABLE_SHOP_INFO + " SET PRICE=" + price + ", INFO=" + info + " where NAME="+name;
            db.execSQL(sql);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void delData(String name){
        name = "\""+name +"\"";

        String sql = "delete from "+TABLE_SHOP_INFO+" where NAME="+name;
        db.execSQL(sql);
//        Toast.makeText(context.getApplicationContext(), name+"이 삭제되었습니다.",Toast.LENGTH_SHORT).show();
    }


    public void createTable(){
        String DROP_SQL = "drop table if exists " + TABLE_SHOP_BASKET;
        try {
            db.execSQL(DROP_SQL);
        } catch(Exception ex) {
            Log.e(TAG, "Exception in DROP_SQL", ex);
        }

        String CREATE_SQL2 = "create table if not exists " + TABLE_SHOP_BASKET + "("
                + "  _id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + "  NAME TEXT, "
                + "  PRICE INTEGER, "
                + "  CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP "
                + ")";
        try {
            db.execSQL(CREATE_SQL2);
//            Toast.makeText(context.getApplicationContext(), "테이블 만듦", Toast.LENGTH_SHORT).show();

        } catch(Exception ex) {
            Log.e(TAG, "Exception in CREATE_SQL", ex);
        }
    }

    public boolean checkDB(String search_name){
        search_name = "\""+search_name +"\"";
        Cursor c1 = db.rawQuery("select NAME from "+TABLE_SHOP_INFO + " where NAME=" + search_name, null);
        if(c1.getCount() > 0){
            return true;
        } else{
            return false;
        }
    }

    public String HashTable_kortoeng(String kor_name){
        Hashtable<String, String> matchTable = new Hashtable<String, String>();
        matchTable.put("타이레놀 500","Tylenol_500");
        matchTable.put("타이레놀 콜드","Tylenol_Cold");

//        matchTable.put("진한초코칩쿠키","Dark_chocolate_chip_cookies");
//        matchTable.put("콘초코플러스","Corn_Choco_Plus");
//        matchTable.put("달콤한소라형과자","Sweet_conch_type_snack");
//        matchTable.put("감튀레드칠리맛","French_fries__Red_chili_flavor");
//        matchTable.put("구운마늘바게트","Grilled_garlic_baguette");
//        matchTable.put("아미고나쵸칩","Amigo_nacho_chip");
//        matchTable.put("계란과자","Egg_snacks");
//        matchTable.put("오란다스낵","Oranda_Snack");
//        matchTable.put("마늘맛콘스낵","Garlic_flavored_corn_snack");
//        matchTable.put("케틀칩케틀콘맛","Soft_egg_snack");
//        matchTable.put("소프트계란과자","Kettle_Chip__Kettle_Corn_Flavor");
//        matchTable.put("작은별초코스낵","Small_star_chocolate_snack");
//        matchTable.put("달고나짱구","Dalgona_Crayon_Shin_Chan");
//        matchTable.put("오구마","Sweet_potatoes");
//        matchTable.put("츄러스","Churros");
//        matchTable.put("스윙칩갈릭디핑소스맛","Swing_chip__Garlic_dipping_sauce");
//        matchTable.put("꼬북칩콘스프맛","Turtle_chip__corn_soup_flavor");
//        matchTable.put("고추칩","Chili_chip");


        String eng_name = matchTable.get(kor_name);
        return eng_name;
    }

    public String HashTable_engtokor(String kor_name){
        Hashtable<String, String> matchTable = new Hashtable<String, String>();
        matchTable.put("콘쵸","Corncho");
        matchTable.put("감자톡허브솔트맛","PotatoTalk_HerbSaltFlavor");
        matchTable.put("감자톡매콤달콤맛","PotatoTalk_spicyandsweet");
        matchTable.put("새우깡","Shrimp_crackers");
        matchTable.put("프레첼","pretzel");
//        matchTable.put("진한초코칩쿠키","Dark_chocolate_chip_cookies");
//        matchTable.put("콘초코플러스","Corn_Choco_Plus");
//        matchTable.put("달콤한소라형과자","Sweet_conch_type_snack");
//        matchTable.put("감튀레드칠리맛","French_fries__Red_chili_flavor");
//        matchTable.put("구운마늘바게트","Grilled_garlic_baguette");
//        matchTable.put("아미고나쵸칩","Amigo_nacho_chip");
//        matchTable.put("계란과자","Egg_snacks");
//        matchTable.put("오란다스낵","Oranda_Snack");
//        matchTable.put("마늘맛콘스낵","Garlic_flavored_corn_snack");
//        matchTable.put("케틀칩케틀콘맛","Soft_egg_snack");
//        matchTable.put("소프트계란과자","Kettle_Chip__Kettle_Corn_Flavor");
//        matchTable.put("작은별초코스낵","Small_star_chocolate_snack");
//        matchTable.put("달고나짱구","Dalgona_Crayon_Shin_Chan");
//        matchTable.put("오구마","Sweet_potatoes");
//        matchTable.put("츄러스","Churros");
//        matchTable.put("스윙칩갈릭디핑소스맛","Swing_chip__Garlic_dipping_sauce");
//        matchTable.put("꼬북칩콘스프맛","Turtle_chip__corn_soup_flavor");
//        matchTable.put("고추칩","Chili_chip");





        String eng_name = matchTable.get(kor_name);
        return eng_name;
    }

    private void println(String msg) {
        Log.d(TAG, msg);
    }


}

