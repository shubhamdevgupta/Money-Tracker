package com.androiddev.moneytracker
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter(private val messages: List<Messages>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val item = messages[position]
        holder.tvCount.text = position.toString()
        holder.tvDate.text = item.date
        holder.tvMsg.text = item.sms
        holder.tvPhoneNo.text = item.number
    }

    override fun getItemCount(): Int = messages.size

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCount: TextView = itemView.findViewById(R.id.tvCount)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvMsg: TextView = itemView.findViewById(R.id.tvMsg)
        val tvPhoneNo: TextView = itemView.findViewById(R.id.tvPhoneno)
    }
}
