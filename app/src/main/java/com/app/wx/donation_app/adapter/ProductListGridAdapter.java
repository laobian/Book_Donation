package com.app.wx.donation_app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.wx.donation_app.R;
import com.app.wx.donation_app.entry.ProjectDetail;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by 112 on 2018/10/5.
 */

public class ProductListGridAdapter extends RecyclerView.Adapter<ProductListGridAdapter.ViewHolder> {

    private Context context;
    private List<ProjectDetail> projectDetailList;
    private OnItemClickListener mOnItemClickListener;

    public ProductListGridAdapter(Context context, List<ProjectDetail> products) {
        this.context = context;
        this.projectDetailList = products;
    }

    public ProductListGridAdapter() {

    }

    /**
     * 创建视图，并绑定ViewHolder
     * @param parent   :
     * @param viewType :
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建行视图
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_project_list_grid, parent, false);

        return new ViewHolder(view);
    }
    /**
     * 配置行布局中控件的数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvProName.setText(projectDetailList.get(position).getProjectName());
        holder.tvProDescription.setText(projectDetailList.get(position).getProjectDescription());
        holder.tvNeedNum.setText("所需数量："+projectDetailList.get(position).getNeedNumber());
        holder.tvReceivedNum.setText("已募捐数量"+projectDetailList.get(position).getReceivedNumber()+"");
        Glide.with(context).load(projectDetailList.get(position).getImageUrl()).into(holder.listIvProject);

        if( mOnItemClickListener!= null){
            holder.itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);

                }
            });
            holder. itemView.setOnLongClickListener( new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(position);
                    //return false;
                    return true;
                }
            });
        }

    }
    /**
     * 配置列表行数
     */
    @Override
    public int getItemCount() {
        return projectDetailList == null ? 0 : projectDetailList.size();
    }

    /**
     * 设置监听
     */
    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }

    public void setmOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }
    /**
     * 定义行布局中的控件，并获取控件
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView listIvProject;
        private TextView tvProName, tvProDescription,tvNeedNum,tvReceivedNum;

        public ViewHolder(View itemView) {
            super(itemView);
            /*获取视图中的控件*/

            listIvProject = itemView.findViewById(R.id.iv_list_project);
            tvProName = itemView.findViewById(R.id.tv_list_project_name);
            tvProDescription = itemView.findViewById(R.id.tv_list_project_description);
            tvNeedNum = itemView.findViewById(R.id.tv_list_need_number);
            tvReceivedNum = itemView.findViewById(R.id.tv_list_received_number);
        }
    }
}
