package com.example.myapplication;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.zebra.sdk.comm.BluetoothConnectionInsecure;
import com.zebra.sdk.comm.Connection;

public class MainActivity extends AppCompatActivity {

    //Connection thePrinterConn = new BluetoothConnectionInsecure(btMacAddress);
    Connection thePrinterConn = new BluetoothConnectionInsecure("");

    String btMacAddress = "ac3fa4c8682d";
    String zplData = "^XA^FO20,20^A0N,25,25^FDThis is a ZPL test.^FS^XZ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btn0 = findViewById(R.id.button0);   //open
        Button btn1 = findViewById(R.id.button1);   //close
        Button btn2 = findViewById(R.id.button2);   //send
        Button btn3 = findViewById(R.id.button3);   //bt status
        Button btn4 = findViewById(R.id.button4);   //open> send zpl > close
        Button btn5 = findViewById(R.id.button5);   //fast print mode

        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prtBtOpen(btMacAddress);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prtBtClose();
            }
        });


        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prtBtSendZpl(zplData);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prtGetPrinterConnIsConnected();
            }
        });


        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendZplOverBluetooth(btMacAddress,zplData);
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendZplOverBluetoothContinuousPrint(btMacAddress,zplData);
            }
        });


    }

    // 印刷機能： 単体処理
    // 印刷用：Bluetooth 接続のステータス取得
    public Boolean prtGetPrinterConnIsConnected() {
        Boolean b = false;
        try{

            b = thePrinterConn.isConnected();

            if (b == true) {
                Log.v("ZZZ", "Bluetooth connection is open.");
            }
            else if (b == false) {
                Log.v("ZZZ", "Bluetooth connection is closed.");
            }
            else {
                Log.v("ZZZ", "Bluetooth connection is in unexpected status");
            }
        }catch(Exception e){
            e.printStackTrace();
            Log.v("ZZZ", e.toString());
        }
        return b;
    }


    // 印刷機能： 単体処理
    // 印刷用：Bluetooth 接続 OPEN
    public void prtBtOpen(final String btMacAddress) {

        new Thread(new Runnable() {
            public void run() {
                try {
                    //Connection thePrinterConn = new BluetoothConnectionInsecure(btMacAddress);
                    thePrinterConn = new BluetoothConnectionInsecure(btMacAddress);

                    // Initialize
                    Looper.prepare();

                    // Open the connection - physical connection is established here.
                    thePrinterConn.open();

                    Looper.myLooper().quit();
                } catch (Exception e) {
                    // Handle communications error here.
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 印刷機能： 単体処理
    // 印刷用：Bluetooth 接続 Close
    public void prtBtClose() {

        new Thread(new Runnable() {
            public void run() {
                try {

                    // Initialize
                    Looper.prepare();

                    // Make sure the data got to the printer before closing the connection
                    Thread.sleep(500);

                    // Close the insecure connection to release resources.
                    thePrinterConn.close();

                    Looper.myLooper().quit();
                } catch (Exception e) {
                    // Handle communications error here.
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 印刷機能： 単体処理
    // 印刷用：Bluetooth経由でZPLを送信
    public void prtBtSendZpl(String zplData) {

        new Thread(new Runnable() {
            public void run() {
                try {

                    // Initialize
                    Looper.prepare();

                    // This example prints "This is a ZPL test." near the top of the label.
                    //String zplData = "^XA^FO20,20^A0N,25,25^FDThis is a ZPL test.^FS^XZ";

                    // Send the data to printer as a byte array.
                    thePrinterConn.write(zplData.getBytes());


                    Looper.myLooper().quit();
                } catch (Exception e) {
                    // Handle communications error here.
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }).start();
    }



    // 一気通貫処理： open > send zpl > close
    // 単枚印刷に最適
    // Close処理は別途必要
    public void sendZplOverBluetooth(final String btMacAddress, String zplData) {

        new Thread(new Runnable() {
            public void run() {
                try {
                    //Connection thePrinterConn = new BluetoothConnectionInsecure(btMacAddress);

                    // Instantiate insecure connection for given Bluetooth&reg; MAC Address.
                    thePrinterConn = new BluetoothConnectionInsecure(btMacAddress);

                    // Initialize
                    Looper.prepare();

                    // Open the connection - physical connection is established here.
                    thePrinterConn.open();

                    // This example prints "This is a ZPL test." near the top of the label.
                    //String zplData = "^XA^FO20,20^A0N,25,25^FDThis is a ZPL test.^FS^XZ";

                    // Send the data to printer as a byte array.
                    thePrinterConn.write(zplData.getBytes());

                    // Make sure the data got to the printer before closing the connection
                    Thread.sleep(500);

                    // Close the insecure connection to release resources.
                    thePrinterConn.close();

                    Looper.myLooper().quit();
                } catch (Exception e) {
                    // Handle communications error here.
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }).start();
    }

    // 一気通貫処理： open(判定) > send zpl
    // 連続・高速印刷に最適
    public void sendZplOverBluetoothContinuousPrint(final String btMacAddress, String zplData) {

        new Thread(new Runnable() {
            public void run() {
                try {
                    //Connection thePrinterConn = new BluetoothConnectionInsecure(btMacAddress);

                    // Instantiate insecure connection for given Bluetooth&reg; MAC Address.
                    //thePrinterConn = new BluetoothConnectionInsecure(btMacAddress);

                    // Initialize
                    Looper.prepare();

                    // Bluetooth 接続が切れている場合は接続をOpen
                    if (!thePrinterConn.isConnected()){
                        // Open the connection - physical connection is established here.
                        thePrinterConn = new BluetoothConnectionInsecure(btMacAddress);
                        thePrinterConn.open();
                        Log.v("ZZZ", "Printer Connection is not opened so PrinterConn.open is executed.");
                    }


                    // This example prints "This is a ZPL test." near the top of the label.
                    //String zplData = "^XA^FO20,20^A0N,25,25^FDThis is a ZPL test.^FS^XZ";

                    // Send the data to printer as a byte array.
                    thePrinterConn.write(zplData.getBytes());

                    // Make sure the data got to the printer before closing the connection
                    //Thread.sleep(500);

                    // Close the insecure connection to release resources.
                    //thePrinterConn.close();

                    Looper.myLooper().quit();
                } catch (Exception e) {
                    // Handle communications error here.
                    e.printStackTrace();
                }
            }
        }).start();
    }


}