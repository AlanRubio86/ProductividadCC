package com.productividadcc.utilerias;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.productividadcc.R;
import com.productividadcc.database.Event;
import com.productividadcc.webservice.Webservice;

public class Tasks extends Activity
{
    private static ProgressDialog waitDlg;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        waitDlg.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        waitDlg.dismiss();
    }

    public static class CheckinTask extends AsyncTask<Void, String, Boolean>
    {
        private static final String TAG = "CheckinTask";

        private Context mContext;
        private Event mEvent;
        private CheckinListener mListener;
        //private ProgressDialog waitDlg;
        private String mMsg = null;

        public CheckinTask(Context c, Event e, CheckinListener l)
        {
            mContext = c;
            mEvent = e;
            mListener = l;

        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            waitDlg = new ProgressDialog(mContext);
            waitDlg.setCancelable(false);
            waitDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            waitDlg.setMessage(mContext.getResources().getString(R.string.checkin_msg));
            waitDlg.setOnCancelListener(new DialogInterface.OnCancelListener()
            {
                public void onCancel(DialogInterface d)
                {
                    Toast.makeText(mContext, R.string.checkin_canceled, Toast.LENGTH_LONG).show();
                }
            });
            waitDlg.show();
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            Log.i(TAG, "onPostExecute: " + result);

            if (waitDlg != null && waitDlg.isShowing() && waitDlg.getWindow() != null)
            {
                //waitDlg.dismiss();
            }
            waitDlg = null;

            if (result != null && result.booleanValue())
            {
                if (mListener != null)
                    mListener.checkinFinished();
            }
            else
            {
                Toast.makeText(mContext, R.string.checkin_error, Toast.LENGTH_LONG).show();
            }
        }

        protected void onProgressUpdate(String... msg)
        {
            try
            {
                if (msg[0] != null)
                {
                    waitDlg.setMessage(msg[0]);
                }
                else
                {
                    if (mMsg != null && mMsg.length() != 0)
                    {
                        Toast toast = Toast.makeText(mContext, mMsg, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                        toast.show();
                    }
                }
            }
            catch (Exception e)
            {

            }
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            com.productividadcc.utilerias.Globales.sleeper(1000);

            long id = mEvent.insert();
            if (id > 0){
                //if(Utils.isWifiOnline(mContext)){
                if (Webservice.LogVisit(mContext, mEvent)){
                    mEvent.delete(id);

                    // Consultamos la informacion de la tabla evaluacion
					/*Cursor cursor = evalEvent.load();
						if (cursor != null && cursor.getCount() > 0){
							if (Webservice.LogEvaluacion(cursor)){
								evalEvent.delete(id);
							}else {
							String tmp = null;
								mMsg = mContext.getResources().getString(R.string.wifi_error) + "\n" + mContext.getResources().getString(R.string.data_saved);
								publishProgress(tmp);
								}
							}*/
                }else {
                    String tmp = null;
                    mMsg = mContext.getResources().getString(R.string.wifi_error) + "\n" + mContext.getResources().getString(R.string.data_saved);
                    publishProgress(tmp);
                }
                //}
                return true;
            }else{
                return false;
            }
        }

    }

    public static class CheckoutTask extends AsyncTask<Void, String, Boolean>
    {
        private static final String TAG = "CheckoutTask";

        private Context mContext;
        private CheckoutListener mListener;
        //private ProgressDialog waitDlg;
        private String mMsg = null;
        private Event mEvent;

        public CheckoutTask(Context c, Event e, CheckoutListener l)
        {
            mContext = c;
            mEvent = e;
            mListener = l;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            waitDlg = new ProgressDialog(mContext);
            waitDlg.setCancelable(false);
            waitDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            waitDlg.setMessage(mContext.getResources().getString(R.string.checkout_msg));
            waitDlg.setOnCancelListener(new DialogInterface.OnCancelListener()
            {
                public void onCancel(DialogInterface d)
                {
                    Toast.makeText(mContext, R.string.checkout_canceled, Toast.LENGTH_LONG).show();
                }
            });
            waitDlg.show();
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            Log.i(TAG, "onPostExecute: " + result);

            if (waitDlg != null && waitDlg.isShowing() && waitDlg.getWindow() != null)
            {
                //waitDlg.dismiss();
            }
            waitDlg = null;

            if (result != null && result.booleanValue())
            {
                // Toast.makeText(mContext, R.string.checkout_ok,
                // Toast.LENGTH_LONG).show();

                if(mListener != null)
                    mListener.checkoutFinished();
            }
            else
            {
                Toast.makeText(mContext, R.string.checkout_error, Toast.LENGTH_LONG).show();
            }
        }

        protected void onProgressUpdate(String... msg)
        {
            try
            {
                if (msg[0] != null)
                {
                    waitDlg.setMessage(msg[0]);
                }
                else
                {
                    if (mMsg != null && mMsg.length() != 0)
                    {
                        Toast toast = Toast.makeText(mContext, mMsg, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                        toast.show();
                    }
                }
            }
            catch (Exception e)
            {

            }
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            com.productividadcc.utilerias.Globales.sleeper(1000);

            long id = mEvent.insert();
            if (id > 0)
            {
                if(com.productividadcc.utilerias.Globales.isWifiOnline(mContext))
                {
                    if (Webservice.LogVisit(mContext, mEvent))
                    {
                        mEvent.delete(id);
                    }
                    else
                    {
                        String tmp = null;
                        mMsg = mContext.getResources().getString(R.string.data_saved);
                        publishProgress(tmp);
                    }
                }
                return true;
            }
            else
            {
                return false;
            }
        }

    }

    public static class UploadTask extends AsyncTask<Void, String, Boolean>
    {
        private static final String TAG = "UploadTask";

        private Context mContext;
        //private ProgressDialog waitDlg;
        private String mMsg = null;
        private UploadListener mListener;
        private int uploaded;

        public UploadTask(Context c, UploadListener l)
        {
            mContext = c;
            mListener = l;
            uploaded = 0;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            waitDlg = new ProgressDialog(mContext);
            waitDlg.setCancelable(false);
            waitDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            waitDlg.setMessage(mContext.getResources().getString(R.string.upload_msg));
            waitDlg.setOnCancelListener(new DialogInterface.OnCancelListener()
            {
                public void onCancel(DialogInterface d)
                {
                    Toast.makeText(mContext, R.string.upload_canceled, Toast.LENGTH_LONG).show();
                }
            });
            waitDlg.show();
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            Log.i(TAG, "onPostExecute: " + result);

            if (waitDlg != null && waitDlg.isShowing() && waitDlg.getWindow() != null)
            {
                //waitDlg.dismiss();
            }
            //waitDlg = null;

            if (uploaded > 0)
            {
                Toast.makeText(mContext, uploaded + " event" + (uploaded > 1 ? "s" : "") + " uploaded", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(mContext, "No event uploaded!", Toast.LENGTH_LONG).show();
            }

            if (mListener != null)
                mListener.uploadFinished();
        }

        protected void onProgressUpdate(String... msg)
        {
            try
            {
                if (msg[0] != null)
                {
                    waitDlg.setMessage(msg[0]);
                }
                else
                {
                    if (mMsg != null && mMsg.length() != 0)
                    {
                        Toast toast = Toast.makeText(mContext, mMsg, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                        toast.show();
                    }
                }
            }
            catch (Exception e)
            {

            }
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            com.productividadcc.utilerias.Globales.log(TAG, "loading from database...");
            com.productividadcc.utilerias.Globales.sleeper(1000);

            Event event = new Event();
            Cursor cursor = event.load();

            int count = 0;
            int total = 0;
            if (cursor != null)
            {
                cursor.moveToFirst();

                total = cursor.getCount();

                while (!cursor.isAfterLast())
                {
                    Event ev = new Event();
                    ev.copy(cursor);

                    com.productividadcc.utilerias.Globales.log(TAG, "Event: " + ev);

                    publishProgress("Uploading... " + (count + 1) + "/" + total);

                    if (Webservice.LogVisit(mContext, ev))
                    {
                        ev.delete();
                        uploaded++;
                    }
                    count++;
                    cursor.moveToNext();
                }
                cursor.close();
            }
            com.productividadcc.utilerias.Globales.log(TAG, "" + uploaded + " event(s) uploaded");

            return true;
        }

    }

    public interface CheckinListener
    {
        public void checkinFinished();

        void onCheckedChanged(View v);
    }

    public interface CheckoutListener
    {
        public void checkoutFinished();
    }

    public interface UploadListener
    {
        public void uploadFinished();
    }
}