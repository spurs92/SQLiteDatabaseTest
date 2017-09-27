package com.spurs.sqlitedatabasetest;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity {

    EditText editName;
    EditText editScore;
    TextView tv;

    SQLiteDatabase db;
    String tableName="rank";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName=(EditText)findViewById(R.id.edit_name);
        editScore=(EditText)findViewById(R.id.edit_score);
        tv=(TextView)findViewById(R.id.tv);

        //data.db라는 파일명의 데이터베이스 파일 생성 or 열기
        db = openOrCreateDatabase("data.db",MODE_PRIVATE,null);

        //rank라는 이름의 테이블(표)를 생성(create)
        db.execSQL("create table if not exists "+tableName+"("
                +"id integer primary key autoincrement,"
                +"name text,"
                +"score integer);");

    }

    public void clickInsert(View v){
        String name=editName.getText().toString();
        int score=0;
        try{
            score=Integer.parseInt(editScore.getText().toString());
        }catch (Exception e){}
        editName.setText("");
        editScore.setText("");

        db.execSQL("insert into "+tableName+"(name, score) values('"+name+"',"+score+")");

    }

    public void clickSelectAll(View v){
        Cursor cursor=db.rawQuery("select * from "+tableName, null);
        if(cursor==null) return;

        StringBuffer buffer=new StringBuffer();

        while(cursor.moveToNext()){
            int id=cursor.getInt(cursor.getColumnIndex("id"));
            String name=cursor.getString(cursor.getColumnIndex("name"));
            int score=cursor.getInt(cursor.getColumnIndex("score"));

            buffer.append(id+" : "+name+" : "+score+"\n");
        }
        tv.setText(buffer.toString());
    }

    public void clickSelectName(View v){
        String name=editName.getText().toString();
        editName.setText("");

        Cursor cursor=db.rawQuery("select name,score from "+tableName+" where name=? and score>?", new String[]{name, 10+""});
        if(cursor==null) return;
        StringBuffer buffer=new StringBuffer();
        while (cursor.moveToNext()){
            String name1=cursor.getString(cursor.getColumnIndex("name"));
            int score=cursor.getInt(cursor.getColumnIndex("score"));
            buffer.append(name1 +"  :  "+ score+"\n");
        }
        tv.setText(buffer.toString());
    }

    public void clickUpdate(View v){
        String name=editName.getText().toString();
        int score=0;
        try{
            score=Integer.parseInt(editScore.getText().toString());
        }catch (Exception e){}
        editName.setText("");
        editScore.setText("");

        db.execSQL("update "+tableName+" set score="+score+" where name=?", new String[]{name});
    }

    public void clickDeleteAll(View v){
        db.execSQL("delete from "+tableName);
    }

    public void clickDeleteName(View v){
        String name=editName.getText().toString();
        db.execSQL("delete from "+tableName+" where name=?", new String[]{name});
    }
}
