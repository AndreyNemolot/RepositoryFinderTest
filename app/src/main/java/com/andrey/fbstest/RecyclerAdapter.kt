package com.andrey.fbstest


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import java.util.ArrayList
import android.view.LayoutInflater
import android.widget.TextView
import com.andrey.fbstest.model.repository.RepoItem
import kotlinx.android.synthetic.main.note_item.view.*


class RecyclerAdapter(private var listener: OnItemClickListener): RecyclerView.Adapter<RecyclerAdapter.NoteHolder>() {
    var repoList = ArrayList<RepoItem>()

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun clearData(){
        this.repoList = ArrayList()
        notifyDataSetChanged()
    }

    fun add(items: ArrayList<RepoItem>) {
        repoList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item, parent, false)
        return NoteHolder(itemView)
    }

    override fun getItemCount(): Int {
        return repoList.size
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val currentNote = repoList[position]
        holder.repoName.text=currentNote.name
        holder.projectLanguage.text=currentNote.language
        holder.bind(listener)
    }

    fun setRepositories(notes: ArrayList<RepoItem>) {
        this.repoList = notes
        notifyDataSetChanged()
    }


    class NoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val repoName:TextView = itemView.repoName
        val projectLanguage:TextView = itemView.projectLanguage

        fun bind(listener: OnItemClickListener?){
            if (listener != null) {
                repoName.rootView.setOnClickListener { _ -> listener.onItemClick(layoutPosition) }
            }
        }

    }

}