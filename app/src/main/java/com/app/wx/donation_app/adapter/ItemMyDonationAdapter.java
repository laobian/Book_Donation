package com.app.wx.donation_app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.wx.donation_app.R;
import com.app.wx.donation_app.dto.DonationDTO;
import com.app.wx.donation_app.entry.ProjectDetail;
import com.app.wx.donation_app.util.DateUtil;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author:wx
 * @Date:2019/3/7
 */
public class ItemMyDonationAdapter extends RecyclerView.Adapter<ItemMyDonationAdapter.ViewHolder>{

    private Context context;
    private List<DonationDTO> donationDTOS;
    private OnItemClickListener mOnItemClickListener;

    /*设置监听*/
    public interface OnItemClickListener{
        void myClick(int position);
        void myOnLongClick(int position);
    }

    public void setmOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    public ItemMyDonationAdapter(Context context, List<DonationDTO> donationDTOList) {
        this.context = context;
        this.donationDTOS = donationDTOList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_my_donation, parent, false);
        return new ViewHolder(view);
    }

    /**
     * 配置行布局中控件的数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String projectName = donationDTOS.get(position).getProjectDetail().getProjectName();
        Log.i("projectName",projectName);
        holder.tvProjectDescription.setText(donationDTOS.get(position).getProjectDetail().getProjectDescription());
        holder.tvProjectName.setText(projectName);
        holder.tvDonationId.setText(donationDTOS.get(position).getDonationId());
        Date date = donationDTOS.get(position).getCreateTime();
        String strDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
        holder.tvCreateTime.setText(strDate);
        holder.tvDonateNumber.setText(donationDTOS.get(position).getDonationNum()+"本");

        Glide.with(context).load(donationDTOS.get(position).getProjectDetail().getImageUrl()).into(holder.ivProject);

        if( mOnItemClickListener!= null){
            holder.itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.myClick(position);
                }
            });
            holder. itemView.setOnLongClickListener( new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.myOnLongClick(position);
                    //return false;
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return donationDTOS == null ? 0 : donationDTOS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivProject;
        private TextView tvProjectName,tvProjectDescription,tvDonationId,tvCreateTime,tvDonateNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            ivProject = itemView.findViewById(R.id.iv_donation_project);
            tvProjectName = itemView.findViewById(R.id.tv_donation_project_name);
            tvProjectDescription = itemView.findViewById(R.id.tv_donation_project_description);
            tvCreateTime = itemView.findViewById(R.id.tv_create_time);
            tvDonationId = itemView.findViewById(R.id.tv_donation_id);
            tvDonateNumber = itemView.findViewById(R.id.tv_donate_number);
        }
    }
}
