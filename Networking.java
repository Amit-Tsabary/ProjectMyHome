package com.example.amitproject11;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

// an async class that runs on a separate thread
// to handle the communication with the server
public class Networking extends AsyncTask<Void, Void, String>
{
    private String suffix;
    private String result;
    private ButtonDisplay buttonDisplay;
    private Activity activity;
    private String id;
    private ArrayList<Integer> devicesInRoom;

    public Networking(String command, Activity activity, String id) {
        this.suffix = command;
        this.activity = activity;
        this.id = id;
    }
    public Networking(String command, Activity activity, String id, ButtonDisplay buttonDisplay) {
        this.suffix = command;
        this.activity = activity;
        this.id = id;
        this.buttonDisplay = buttonDisplay;
    }
    public Networking(String command, Activity activity, String id, ArrayList<Integer> devicesInRoom) {
        this.suffix = command;
        this.activity = activity;
        this.id = id;
        this.devicesInRoom = devicesInRoom;
    }
    public Networking(String command, Activity activity) {
        this.suffix = command;
        this.activity = activity;
    }
    public Networking(String command, String id) {
        this.suffix = command;
        this.id = id;
    }
    public Networking(String command, ButtonDisplay buttonDisplay) {
        this.suffix = command;
        this.buttonDisplay = buttonDisplay;
    }
    public Networking(String command, ButtonDisplay buttonDisplay, ArrayList<Integer> devicesInRoom) {
        this.suffix = command;
        this.buttonDisplay = buttonDisplay;
        this.devicesInRoom = devicesInRoom;
    }

    // a class that sends requests to the server and deals with the response in t side thread
    public Networking(String command) {
        this.suffix = command;
    }

    public String doInBackground(Void... voids) {
        try {
            URL url = new URL("http://10.0.0.35:8000/"+suffix);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            InputStream input = con.getInputStream();
            //convert Inputstream to String
            Scanner s = new Scanner(input).useDelimiter("\\A");
            result = s.hasNext() ? s.next() : "";
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if(suffix.contains("login_"))
        {
            if (s.startsWith("true_")) {
                System.out.println("Correct");
                //Intent switchActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
                //startActivity(switchActivityIntent);
                String[] values=s.split("_");
                Intent i;
                if(values[4].equals("false"))
                {
                    i = new Intent(activity, MainActivity.class);
                    //name_email_id_da
                    i.putExtra("key",values[1]+"_"+values[2]+"_"+values[3]+"_"+values[4]);
                }
                else
                {
                    i = new Intent(activity, DoubleAuthentication.class);
                    //name_email_id
                    i.putExtra("key",values[1]+"_"+values[2]+"_"+values[3]);
                }

                activity.startActivity(i);
                activity.finish();

                //setContentView(R.layout.activity_main);
                //Toast.makeText(getApplicationContext(), "Redirecting...", Toast.LENGTH_SHORT).show();
            } else {
                TextView errmsg = activity.findViewById(R.id.error_msg);
                errmsg.setText(s);
                errmsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_error_outline_24, 0, 0, 0);
            }
        }
        if (suffix.contains("create_"))
        {
            if (s.startsWith("true"))
            {
                activity.finish();
            }
            else if (s.contains("false_emailExists"))
            {
                TextView errmsg = activity.findViewById(R.id.error_msg_crate_account);
                errmsg.setText(" Account already exists.");
                errmsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_error_outline_24, 0, 0, 0);
            }
        }
        if (suffix.contains("getDevices"))
        {
            if(s.length() > 0) {
                String[] rows = s.split("__");
                for (int i = 0; i < rows.length; i++)
                {
                    if(rows[i].split("_")[0].equals(id))
                    {
                        if(activity instanceof MainActivity || activity instanceof ManageRoom) {
                            Intent intent = new Intent(activity, DeviceControl.class);
                            intent.putExtra("key",rows[i]);
                            //intent.putExtra("superClass", );
                            activity.startActivity(intent);
                        }
                    }
                }

            }

        }
        if (suffix.contains("checkCode_")) {
            if (s.contains("true")) {
                String[] values = s.split("_");
                Intent intent = new Intent(activity, MainActivity.class);
                intent.putExtra("key", values[1] + "_" + values[2] + "_" + values[3] + "_" + values[4]);
                activity.startActivity(intent);
            }
            else
            {
                TextView errmsg = activity.findViewById(R.id.error_msg_DA);
                errmsg.setText(" Incorrect code");
                errmsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_error_outline_24, 0, 0, 0);
            }
        }

        if((suffix.contains("displayAccounts")|| suffix.contains("displayDevById_"))|| suffix.contains("displayDevices"))
        {
            if(s.length() > 0) {
                String[] rows = s.split("__");
                for (int i = 0; i < rows.length; i++) {
                    if(devicesInRoom == null) {
                        buttonDisplay.createButton(rows[i]);
                    }
                    else
                    {
                        boolean canShow = true;
                        for (Integer ID : devicesInRoom) {
                            if(ID == Integer.parseInt(rows[i].split("_")[0]))
                            {
                                canShow = false;
                            }
                        }
                        if(canShow)
                        {
                            buttonDisplay.createButton(rows[i]);
                        }
                    }
                }
            }
        }
        if(suffix.contains("displayRooms"))
        {
            if(s.length() > 0) {
                String[] rows = s.split("____");
                for (int i = 0; i < rows.length; i++) {
                    buttonDisplay.createButton(rows[i]);
                }
            }
        }
        if(suffix.contains("getAccounts"))
        {
            if(s.length() > 0) {
                String[] rows = s.split("__");
                for (int i = 0; i < rows.length; i++)
                {
                    if(rows[i].split("_")[0].equals(id))
                    {
                        Intent intent = new Intent(activity, AccountManagement.class);
                        intent.putExtra("key",rows[i]);
                        activity.startActivity(intent);
                    }
                }
            }
        }
        if(suffix.contains("getRooms"))
        {
            if(s.length() > 0) {
                String[] rows = s.split("____");
                for (int i = 0; i < rows.length; i++)
                {
                    if(rows[i].split("__")[0].equals(id))
                    {
                        Intent intent = new Intent(activity, ManageRoom.class);
                        intent.putExtra("key",rows[i]);
                        activity.startActivity(intent);
                    }
                }

            }
        }
    }
}
