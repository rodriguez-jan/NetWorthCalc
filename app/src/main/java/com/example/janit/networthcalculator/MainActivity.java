package com.example.janit.networthcalculator;

import android.content.SharedPreferences;
import android.database.Cursor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class MainActivity extends AppCompatActivity implements ExampleDialog.ExampleDialogListener,DeleteDialog.DeleteDialogListener  {

    private Button update;
    private TextView total;
    private String toast = "Invalid amount";
    private double sum =0;
    private String temp;
    DatabaseHelper myDb;
    private int id;
    private Button delete;
    private double tempX = 0;
    private double tempY = 0;
    LineGraphSeries<DataPoint> series = new LineGraphSeries<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);
        Cursor res =myDb.getAllRows();
        GraphView graph = (GraphView) findViewById(R.id.graph);
        this.generateGraph(res);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(this.getLastRow(res));
        graph.getGridLabelRenderer().setHumanRounding(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);
        total = (TextView)findViewById(R.id.total);
        update = (Button) findViewById(R.id.button);
        delete = (Button) findViewById(R.id.button2);
        graph.addSeries(series);
        this.getLast(res);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog();
            }
        });

    }
    public void openDialog(){
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    public void deleteDialog(){
        DeleteDialog deleteDialog = new DeleteDialog();
        deleteDialog.show(getSupportFragmentManager(),"delete dialog");
    }


    @Override
    public void applyTexts(String assets, String liab) {
        try {
            if(!assets.equals("")){
                sum += Double.parseDouble(assets);
            }
            if(!liab.equals("")) {
                sum -= Double.parseDouble(liab);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),toast,Toast.LENGTH_LONG).show();
        }
        temp = String.format("%.2f",sum);
        total.setText(temp);
        myDb.insertData(sum);
        Cursor res =myDb.getAllRows();
        res.moveToLast();
        this.addPoint(res);

    }

    @Override
    public void deleteDB(){
        myDb.deleteData();
    }


    public void addPoint(Cursor res){
        if(myDb!= null){

            tempX = Double.parseDouble(res.getString(0)) -1;
            tempY = Double.parseDouble(res.getString(1));
            series.appendData(new DataPoint(tempX,tempY),true,1000);
        }
    }

    public void generateGraph(Cursor res){
        if(res != null && res.getCount() >0){
            for(res.moveToFirst();!res.isAfterLast();res.moveToNext()){
                this.addPoint(res);
            }
        }
    }

    public void getLast(Cursor res){
        if(res != null && res.getCount() >0){
            res.moveToLast();
            sum = Double.parseDouble(res.getString(1));
            temp = String.format("%.2f",sum);
            total.setText(temp);
        }
    }

    public int getLastRow(Cursor res){
        if(res!= null && res.getCount()>0){
            res.moveToLast();
            id = Integer.parseInt(res.getString(0));
            return id -1;
        }
        else{
            return 0;
        }
    }

}
