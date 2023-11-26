package com.example.dodamdodam.ui.more.managefamily

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dodamdodam.databinding.ItemFamilyManageBinding
import com.example.dodamdodam.model.Family
import com.example.dodamdodam.model.FamilyMember

class FamilyMemberAdapter(private val members: List<Family>) : RecyclerView.Adapter<FamilyMemberAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemFamilyManageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(member: Family) {
            binding.textViewName.text = member.name
            binding.textViewPhone.text = member.email
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFamilyManageBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(members[position])
    }

    override fun getItemCount() = members.size
}
