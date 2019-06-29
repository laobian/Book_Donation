package com.app.wx.donation_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.wx.donation_app.R;
import com.app.wx.donation_app.activity.SearchActivity;
import com.app.wx.donation_app.entry.OrderDetail;
import com.app.wx.donation_app.entry.ProjectDetail;
import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.util.List;

import lombok.Data;

/**
 * @Author:wx
 * @Date:2019/3/7
 */
@Data
public class ItemOrderAdapter extends RecyclerView.Adapter<ItemOrderAdapter.ViewHolder>{

    private Context context;
    private List<OrderDetail> orderDetailList;
    private OnItemClickListener mOnItemClickListener;
    private IUpdateOrder updateOrder;


    /*设置监听*/
    public interface OnItemClickListener{
        void myClick(int position);
        void myOnLongClick(int position);
    }

    public void setmOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    public ItemOrderAdapter(Context context, List<OrderDetail> orderDetails) {
        this.context = context;
        this.orderDetailList = orderDetails;
    }

    public ItemOrderAdapter(Context context, List<OrderDetail> orderDetailList, IUpdateOrder updateOrder) {
        this.context = context;
        this.orderDetailList = orderDetailList;
        this.updateOrder = updateOrder;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    /**
     * 配置行布局中控件的数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvProjectName.setText(orderDetailList.get(position).getProjectName());
        holder.tvOrderId.setText(orderDetailList.get(position).getOrderId());
        int orderStatue = orderDetailList.get(position).getOrderStatue();
        if (orderStatue ==0 ){
            holder.tvOrderStatue.setText("待寄出");
        }else if (orderStatue == 2){
            holder.tvOrderStatue.setText("已取消");
            holder.btnSend.setVisibility(View.INVISIBLE);
            holder.btnCancel.setVisibility(View.INVISIBLE);
            holder.tvStatueDescription.setText("订单状态:订单已取消");
            holder.tvStatueDescription.setVisibility(View.VISIBLE);
        }else {
            holder.tvOrderStatue.setText("已寄出");
            holder.btnSend.setText("您已寄出");
            holder.btnSend.setVisibility(View.INVISIBLE);
            holder.btnCancel.setVisibility(View.INVISIBLE);
            holder.tvStatueDescription.setText("订单状态:捐赠书籍已寄出");
            holder.tvStatueDescription.setVisibility(View.VISIBLE);
        }
        holder.tvDonationDetail.setText(orderDetailList.get(position).getBookName()
                +orderDetailList.get(position).getDonationNum()+"本");
        Glide.with(context).load(orderDetailList.get(position).getImageUrl()).into(holder.ivProject);

        holder.btnCancel.setOnClickListener(new MyListen(position));
        holder.btnSend.setOnClickListener(new MyListen(position));
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
        return orderDetailList == null ? 0 : orderDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivProject;
        private TextView tvProjectName, tvDonationDetail, tvOrderStatue, tvOrderId,tvStatueDescription;
        private Button btnCancel, btnSend;

        public ViewHolder(View itemView) {
            super(itemView);
            ivProject = itemView.findViewById(R.id.iv_order_project);
            tvProjectName = itemView.findViewById(R.id.tv_order_project_name);
            tvDonationDetail = itemView.findViewById(R.id.tv_donation_detail);
            tvOrderId = itemView.findViewById(R.id.tv_orderId);
            tvOrderStatue = itemView.findViewById(R.id.tv_order_statue);
            tvStatueDescription = itemView.findViewById(R.id.tv_statue_description);

            btnCancel = itemView.findViewById(R.id.btn_order_cancel);
            btnSend = itemView.findViewById(R.id.btn_send);
        }
    }

    private class MyListen implements View.OnClickListener {

        int position;

        public MyListen(int position) {
            // TODO Auto-generated constructor stub
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            int orderStatue = 0;
            switch (v.getId()){
                case R.id.btn_order_cancel:
                    orderStatue = 2;
                    break;
                case R.id.btn_send:
                    orderStatue = 1;
                    break;
            }
            updateOrder.update(position,orderStatue);
        }
    }
    public interface IUpdateOrder{

        void update(int position,int projectStatue);
    }

}
