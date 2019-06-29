package com.app.wx.donation_app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.wx.donation_app.R;
import com.app.wx.donation_app.entry.ProjectDetail;
import com.bumptech.glide.Glide;

import java.util.List;

import lombok.Data;

/**
 * @Author:wx
 * @Date:2019/3/7
 */
@Data
public class ItemProjectAdapter extends RecyclerView.Adapter<ItemProjectAdapter.ViewHolder>{

    private Context context;
    private List<ProjectDetail> projectDetailList;
    private OnItemClickListener mOnItemClickListener;

    /*设置监听*/
    public interface OnItemClickListener{
        void myClick( int position);
        void myOnLongClick( int position);
    }

    public void setmOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    public ItemProjectAdapter(Context context, List<ProjectDetail> projectDetails) {
        this.context = context;
        this.projectDetailList = projectDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.rv_project_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * 配置行布局中控件的数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvProjectName.setText(projectDetailList.get(position).getProjectName());
        holder.tvProjectDescription.setText(projectDetailList.get(position).getProjectDescription());
        holder.tvNeedNumber.setText("所需数量："+projectDetailList.get(position).getNeedNumber());
        holder.tvReceivedNumber.setText("已募捐数量"+projectDetailList.get(position).getReceivedNumber()+"");
        Glide.with(context).load(projectDetailList.get(position).getImageUrl()).into(holder.ivProject);

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
        return projectDetailList == null ? 0 : projectDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivProject;
        private TextView tvProjectName,tvProjectDescription,tvNeedNumber,tvReceivedNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            ivProject = itemView.findViewById(R.id.iv_project);
            tvProjectName = itemView.findViewById(R.id.tv_project_name);
            tvProjectDescription = itemView.findViewById(R.id.tv_project_description);
            tvNeedNumber = itemView.findViewById(R.id.tv_need_number);
            tvReceivedNumber = itemView.findViewById(R.id.tv_received_number);
        }

    }
}
