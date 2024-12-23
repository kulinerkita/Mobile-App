package com.capstone.kulinerkita.ui.detailResto

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.kulinerkita.data.model.OperatingHours
import com.capstone.kulinerkita.databinding.ItemOperationalHoursBinding

class OperationalHoursAdapter(private val operationalHours: OperatingHours) :
    RecyclerView.Adapter<OperationalHoursAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemOperationalHoursBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(day: String, time: String) {
            binding.tvDay.text = day
            binding.tvTime.text = time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemOperationalHoursBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind("Hari", "${operationalHours.opening_time} - ${operationalHours.closing_time}")
    }

    override fun getItemCount(): Int = 1
}
