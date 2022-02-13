package com.example.myapp;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;


public class AppAdapter extends RecyclerView.Adapter<AppAdapter.MyViewHolder> {

    public interface OnDeleteClickListener {
        void OnDeleteClickListener(App app);
    }

    private Context context;
    private List<App> appList;
    public OnDeleteClickListener onDeleteClickListener;
    private final int ONE_DAY = 24 * 60 * 60 * 1000;

    public AppAdapter(Context context, OnDeleteClickListener listener) {
        this.context = context;
        this.onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        App app = appList.get(position);
        holder.setData(dataconverter.bitimage(app.getImage()), app.getName(), app.getDate(), app.getSave(), position);
        holder.setListeners();

    }

    @Override
    public int getItemCount() {
        if (appList != null)
            return appList.size();
        else return 0;
    }

    public void setApps(List<App> apps) {
        appList = apps;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView contentsText, dateText, text;
        private ImageView image, delete;
        private int Position;
        private static final String TAG = "abc";


        public MyViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            contentsText = itemView.findViewById(R.id.contentsText);
            dateText = itemView.findViewById(R.id.dateText);
            text = itemView.findViewById(R.id.saves);
            delete = itemView.findViewById(R.id.delete);
        }

        public void setData(Bitmap images, String contentsTexts, String dateTexts, String texts, int position) {
            final String index1 = dateTexts.substring(dateTexts.indexOf("-") + 1, dateTexts.indexOf("-") + 3);

            image.setImageBitmap(images);
            contentsText.setText(contentsTexts);
            dateText.setText(getDday(Integer.parseInt(dateTexts.substring(0, dateTexts.indexOf("-"))),
                    Integer.parseInt(index1) - 1, Integer.parseInt(dateTexts.substring(dateTexts.indexOf("-") + 4))));
            text.setText(texts);
            Position = position;

        }


        private String getDday(int yy, int mm, int dd) {
            // D-day 설정
            final Calendar ddayCalendar = Calendar.getInstance();
            ddayCalendar.set(yy, mm, dd);

            // D-day 를 구하기 위해 millisecond 으로 환산하여 d-day 에서 today 의 차를 구한다.
            final long dday = ddayCalendar.getTimeInMillis() / ONE_DAY;
            final long today = Calendar.getInstance().getTimeInMillis() / ONE_DAY;
            long result = dday - today;

            // 출력 시 d-day 에 맞게 표시
            final String strFormat;
            if (result > 0) {
                strFormat = "D-%d";
            } else if (result == 0) {
                strFormat = "D-Day";
            } else {
                result *= -1;
                strFormat = "D+%d";
            }

            final String strCount = (String.format(strFormat, result));
            return strCount;
        }

        public void setListeners() {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Update.class);
                    intent.putExtra("id", appList.get(Position).getId());
                    ((Activity) context).startActivityForResult(intent, MainActivity.UPDATE_APP_ACTIVITY_REQUEST_CODE);
                    ((Activity) context).overridePendingTransition(R.anim.in,R.anim.out);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteDialog();
                }
            });
        }

        private void deleteDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("삭제 확인");
            builder.setMessage("삭제하시겠습니까?");
            builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.OnDeleteClickListener(appList.get(Position));
                    }
                }
            });
            builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();
        }

    }

}










