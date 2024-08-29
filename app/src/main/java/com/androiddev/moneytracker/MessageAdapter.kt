package com.androiddev.moneytracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter(val context: Context, val messageList: ArrayList<MessagesModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val MESSAGE_SEND: Int = 1
        const val MESSAGE_RECIVE: Int = 2
    }

    inner class SendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(messages: MessagesModel) {
            val msg = itemView.findViewById<TextView>(R.id.tv_msg_send)
                msg.text = messages.message
            }
        }


    inner class ReciveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(messages: MessagesModel) {
            val msg = itemView.findViewById<TextView>(R.id.tv_msg_recive)
                msg.text = messages.message
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == MESSAGE_SEND) {
            val view = LayoutInflater.from(context).inflate(R.layout.message_sent, parent, false)
            SendViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.message_recived, parent, false)
            ReciveViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (messageList.get(position).isOutgoing) {
            MESSAGE_SEND
        } else
            MESSAGE_RECIVE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = messageList[position]
        if (holder is SendViewHolder) {
            holder.bind(msg)
        } else if (holder is ReciveViewHolder) {
            holder.bind(msg)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}