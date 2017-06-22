package com.aseman.bbk;

import java.util.List;
import com.squareup.picasso.Picasso;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterSections extends RecyclerView.Adapter<AdapterSections.ViewHolder> {
	private List<ImageList> mDataset;

	public class ViewHolder extends RecyclerView.ViewHolder {
		public TextView txtHeader;
		public TextView txtFooter;
		ImageView img;
		Context c;

		public ViewHolder(View v) {
			super(v);
			txtHeader = (TextView) v.findViewById(R.id.Text1);
			//txtFooter = (TextView) v.findViewById(R.id.secondLine);
			img = (ImageView) v.findViewById(R.id.ImageV);
			c=v.getContext();
		}
	}
	public void update(List<ImageList> New)
	{
		mDataset.clear();
		mDataset=New;
		notifyDataSetChanged();
	}
	public AdapterSections(List<ImageList> myDataset) {
		mDataset = myDataset;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_child, 
										parent, false);
		ViewHolder vh = new ViewHolder(v);
		return vh;
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {

		//final String name = mDataset.get(position).Title;


		if (mDataset.get(position).iv.getDrawable() != null)
			holder.img.setImageDrawable(mDataset.get(position).iv.getDrawable());
		else
		if (mDataset.get(position).Path!= null)
		{
			Picasso.with(holder.c) //
					.load("http://www.ferzmarket.com/uploads/sections/"+mDataset.get(position).Path) //
					//.placeholder(R.drawable.i1) //
					.error(R.drawable.ic_launcher) //
					.fit() //
					.tag(holder.c) //
					.into(holder.img);
		}

        holder.txtHeader.setText("حذف");
		holder.txtHeader.setOnClickListener(new OnClickListener() {
		        @Override
		        public void onClick(View v) {
					mDataset.remove(position);
					notifyDataSetChanged();
				}
		    });
		

	}


	@Override
	public int getItemCount() {
		return mDataset.size();
	}

}