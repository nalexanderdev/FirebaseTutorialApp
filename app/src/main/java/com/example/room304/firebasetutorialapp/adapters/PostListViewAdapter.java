package com.example.room304.firebasetutorialapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.room304.firebasetutorialapp.R;
import com.example.room304.firebasetutorialapp.models.Post;

import java.util.List;

/**
 * Created by Benit Kibabu on 13/06/2017.
 */

public class PostListViewAdapter extends ArrayAdapter<Post>{
    //List<Post> posts;
    int resource;
    List<Post> posts;
    private Context context;
    public PostListViewAdapter(Context context, int resource, List<Post> postList) {
        super(context, resource, postList);
        this.resource = resource;
        this.context = context;
        this.posts = postList;
    }


    public void updateList(List<Post> postList){
        posts.clear();
        posts.addAll(postList);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Post post = posts.get(position);
        PostHolder holder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, parent, false);

            holder = new PostHolder();
            holder.nameField = (TextView) convertView.findViewById(R.id.nameText);
            holder.postField = (TextView) convertView.findViewById(R.id.postText);

            convertView.setTag(holder);
        }else{
            holder = (PostHolder) convertView.getTag();
        }

        holder.nameField.setText(post.getUsername());
        holder.postField.setText(post.getPost());

        return convertView;
    }

    static class PostHolder{
        TextView nameField;
        TextView postField;
    }

}
