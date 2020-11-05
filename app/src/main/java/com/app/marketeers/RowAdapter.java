package com.app.marketeers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RowAdapter extends RecyclerView.Adapter<RowAdapter.ViewHolder> {
    Context context;
    List<Rowlist> list;

    public RowAdapter(Context context, List<Rowlist> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
holder.tv_title.setText(list.get(position).getTitle());
        String result = list.get(position).getContent().replaceAll("\n ", "\n");

holder.tv_content.setText(result);

     /*   Bitmap bitmapImage = BitmapFactory.decodeFile(list.get(position).getImage());
        int nh = (int) ( bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()) );
        Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
        holder.iv_image.setImageBitmap(scaled);*/
        Picasso.get().load(list.get(position).getImage()).into(holder.iv_image);

        holder.cd_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,SinglePostActivity.class);
                intent.putExtra("postid",list.get(position).getId());
                intent.putExtra("posttitle",list.get(position).getTitle());
                intent.putExtra("postimage",list.get(position).getImage());
                intent.putExtra("postcontent",list.get(position).getContent());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_image;
        CardView cd_layout;
        TextView tv_title,tv_content;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_image=itemView.findViewById(R.id.iv_image);
            tv_title=itemView.findViewById(R.id.tv_title);
            tv_content=itemView.findViewById(R.id.tv_content);
            cd_layout=itemView.findViewById(R.id.cd_layout);
        }
    }
}
