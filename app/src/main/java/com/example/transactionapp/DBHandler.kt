import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.sql.Date

class DBHandler(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    private val context:Context?
    init {
        this.context=context
    }
    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + EMAIL_COL + " TEXT,"
                + PASSWORD_COL + " TEXT)")
        db.execSQL(query)
        //userTable(db)
        Log.d("jjj"," In DBonCreate Method ")
    }

    fun addNewUser(userName: String, userEmail: String, userPassword: String) : String {
        val returnedString: String = checkUserExistance(userEmail)
        if(returnedString.equals("NOT_EXIST")) {
            val db = this.writableDatabase
            val values = ContentValues()
            values.put(NAME_COL, userName)
            values.put(EMAIL_COL, userEmail)
            values.put(PASSWORD_COL, userPassword)
            db.insert(TABLE_NAME, null, values)
            db.close()
            createCreditTable(userEmail)
            createDebitTable(userEmail)
            return "ADDED"
        }
        else
            return "EXIST"
    }

    fun checkUserExistance(vararg UserData: String): String{
        val db: SQLiteDatabase = this.readableDatabase
//        Log.d("kkk","" + UserData.get(1))
//        val cursor: Cursor=db.query(
//            "UserInfo",
//             if (UserData.size > 1) arrayOf(EMAIL_COL,PASSWORD_COL) else arrayOf(EMAIL_COL),
//             if (UserData.size > 1) "email=? AND password=?" else "email=?",
//             if (UserData.size > 1) arrayOf(UserData.get(0),UserData.get(1)) else arrayOf(UserData.get(0)),
//            null,
//            null,
//            null)
        val cursor: Cursor=db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "+ " email='${UserData.get(0)}';" ,null)
        if(cursor.moveToFirst()){
            //Log.d("kkk","line 49 " + cursor.getString(2)+" "+UserData.get(0))
            if(cursor.getString(2).equals(UserData.get(0))) {
                //Log.d("kkk","line 51")
                if (UserData.size > 1) {
                    //Log.d("kkk","line 53")
                    if(cursor.getString(3).equals(UserData.get(1))) {
                        //Log.d("kkk","line 55")
                        cursor.close()
                        return "EXIST"
                    }
                    else{
                        //Log.d("kkk","line 59")
                        cursor.close()
                        return "WRONG_PASSWORD"
                    }
                }
                else{
                    cursor.close()
                    return "EXIST"
                }
            }
            else{
                cursor.close()
                return "NOT_EXIST"
            }
        } else {
            //Log.d("kkk","cursor.moveToFirst()="+ cursor.moveToFirst())
            cursor.close()
            return "NOT_EXIST"
        }
    }

    fun getData():String{
        val str: String
        val db: SQLiteDatabase = this.readableDatabase
        val cursor: Cursor =db.query(
            "UserInfo",
            arrayOf("name","email","password"),
            "id=?",
             arrayOf(Integer.toString(1)),
            null, null, null
        )
        if(cursor.moveToFirst()){
            str="Name= " + cursor.getString(0) + " Email= " + cursor.getString(1) + " Password= " + cursor.getString(2)
            cursor.close()
        }else{
            cursor.close()
            return "NO_TABLE"
        }
        db.close()
        return str
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun deleteTable(){
        this.readableDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
    }

    fun deleteData(str: String){
        val db: SQLiteDatabase=this.writableDatabase
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE name='$str';")
        db.close()
    }

    fun deleteDataFromCredit(email: String, date: String, desc: String, amount: Long){
        val db: SQLiteDatabase=this.writableDatabase
        var CREDIT_TABLE_NAME=email+"CreditTable"
        CREDIT_TABLE_NAME=removeSpecialChar(CREDIT_TABLE_NAME)
        db.execSQL("DELETE FROM " + CREDIT_TABLE_NAME + " WHERE date='$date' AND description='$desc' AND amount='$amount';")
        db.close()
        Log.d("jjj"," deleted ")
    }

    fun deleteDataFromDebit(email: String, date: String, desc: String, amount: Long){
        val db: SQLiteDatabase=this.writableDatabase
        var DEBIT_TABLE_NAME=email+"DebitTable"
        DEBIT_TABLE_NAME=removeSpecialChar(DEBIT_TABLE_NAME)
        db.execSQL("DELETE FROM " + DEBIT_TABLE_NAME + " WHERE date='$date' AND description='$desc' AND amount='$amount';")
        db.close()
        Log.d("jjj"," deleted")
    }

    fun userTable(db: SQLiteDatabase){

    }

    fun createDebitTable(userEmail: String){
        var DEBIT_TABLE_NAME=userEmail+"DebitTable"
        DEBIT_TABLE_NAME=removeSpecialChar(DEBIT_TABLE_NAME)
        val query = ("CREATE TABLE " + DEBIT_TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DATE_COL + " TEXT,"
                + DESCRIPTION_COL + " TEXT,"
                + AMOUNT_COL + " TEXT)")
        this.writableDatabase.execSQL(query)
        Log.d("jjj"," debit table created ")
    }

    public fun removeSpecialChar(str: String): String{
            var s=str
            var i = 0
            while (i < s.length) {

                if (s.get(i) < '0' || s.get(i) > '9' &&
                    s.get(i) < 'A' || s.get(i) > 'Z' &&
                    s.get(i) < 'a' || s.get(i) > 'z'
                ) {
                    s = s.substring(0, i) + s.substring(i + 1)
                    i--
                }
                i++
            }
        return s
    }

    fun createCreditTable(userEmail: String){
        var CREDIT_TABLE_NAME=userEmail+"CreditTable"
        CREDIT_TABLE_NAME=removeSpecialChar(CREDIT_TABLE_NAME)
        val query = ("CREATE TABLE " + CREDIT_TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DATE_COL + " TEXT,"
                + DESCRIPTION_COL + " TEXT,"
                + AMOUNT_COL + " TEXT)")
        this.writableDatabase.execSQL(query)
        Log.d("jjj"," credit table created ")
    }

    fun addCredit(userEmail: String, desc: String, amou: String): Long{
        var CREDIT_TABLE_NAME=userEmail+"CreditTable"
        CREDIT_TABLE_NAME=removeSpecialChar(CREDIT_TABLE_NAME)
        val millis = System.currentTimeMillis()
        val date = Date(millis).toString()
        val values = ContentValues()
        values.put(DATE_COL,date)
        values.put(DESCRIPTION_COL,desc)
        values.put(AMOUNT_COL,amou)
        val db=this.writableDatabase
        val check=db.insert(CREDIT_TABLE_NAME,null,values)
        db.close()
        return check
    }

    fun addDebit(userEmail: String,desc: String, amou: String): Long{
        var DEBIT_TABLE_NAME=userEmail+"DebitTable"
        DEBIT_TABLE_NAME=removeSpecialChar(DEBIT_TABLE_NAME)
        val millis = System.currentTimeMillis()
        val date = Date(millis).toString()
        val values = ContentValues()
        values.put(DATE_COL,date)
        values.put(DESCRIPTION_COL,desc)
        values.put(AMOUNT_COL,amou)
        val db=this.writableDatabase
        val check=db.insert(DEBIT_TABLE_NAME,null,values)
        db.close()
        return check
    }

    fun calculateTotal(): Long{
        val db= this.readableDatabase
        val sharPref=context?.getSharedPreferences("TransactionAppPreferences",
            AppCompatActivity.MODE_PRIVATE
        )
        var TABLE_NAME=sharPref?.getString("email","emailNotGetted").toString()
        TABLE_NAME=DBHandler(context).removeSpecialChar(TABLE_NAME)
        var debitAmount=totalPriceFromDebit(TABLE_NAME,db)
        var creditAmount=totalPriceFromCredit(TABLE_NAME,db)

        return creditAmount-debitAmount
    }

    fun totalPriceFromDebit(tableName: String,db: SQLiteDatabase): Long{
        var totalAmount: Long=0
        val TABLE_NAME=tableName+"DebitTable"
        val cur=db.rawQuery("SELECT amount FROM " + TABLE_NAME + ";" ,null)
        if(cur.moveToFirst()){
            totalAmount+=cur.getString(0).toLong()
            //Log.d("jjj"," line 44 " + cur.getString(0))
            while(cur.moveToNext()){
                totalAmount+=cur.getString(0).toLong()
                //Log.d("jjj"," line 47 " + cur.getString(0))
            }
        }
        else
            return 0
        //Log.d("jjj"," line 47 " + totalAmount)
        return totalAmount
    }

    fun totalPriceFromCredit(tableName: String,db: SQLiteDatabase): Long{
        var totalAmount: Long=0
        val TABLE_NAME=tableName+"CreditTable"
        val cur=db.rawQuery("SELECT amount FROM " + TABLE_NAME + ";" ,null)
        if(cur.moveToFirst()){
            totalAmount+=cur.getString(0).toLong()
            //Log.d("jjj"," line 44 " + cur.getString(0))
            while(cur.moveToNext()){
                totalAmount+=cur.getString(0).toLong()
                //Log.d("jjj"," line 47 " + cur.getString(0))
            }
        }
        else
            return 0
        //Log.d("jjj"," line 47 " + totalAmount)
        return totalAmount
    }

    fun updateData(name: String, email: String, password: String): String{

//        val returnedString: String = checkUserExistance(email)
//        if(returnedString.equals("NOT_EXIST")) {
            val db = this.writableDatabase
            val query=(" UPDATE UserInfo SET name='$name',password='$password' WHERE email='$email';")
            db.execSQL(query)
            db.close()
//            createCreditTable(email)
//            createDebitTable(email)
            return "UPDATED"
//        }
//        else
//            return "EXIST"

    }

    companion object {
        private const val DB_NAME = "TransactionDb"
        private const val DB_VERSION = 1
        private const val TABLE_NAME = "UserInfo"
        private const val ID_COL = "id"
        private const val NAME_COL = "name"
        private const val EMAIL_COL = "email"
        private const val PASSWORD_COL = "password"

        private const val DATE_COL="date"
        private const val DESCRIPTION_COL="description"
        private const val AMOUNT_COL="amount"
    }

}