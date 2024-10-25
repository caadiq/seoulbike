package com.beemer.seoulbike.view.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.beemer.seoulbike.R
import com.beemer.seoulbike.databinding.BottomsheetdialogStationStatusBinding
import com.beemer.seoulbike.model.dto.StationListDto
import com.beemer.seoulbike.view.utils.DateTimeConverter.convertDateTime
import com.beemer.seoulbike.view.utils.UnitConversion.formatDistance
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Locale

class StationStatusBottomsheetDialog(private val item: StationListDto) : BottomSheetDialogFragment() {
    private var _binding: BottomsheetdialogStationStatusBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomsheetdialogStationStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        item.stationStatus.updateTime?.let { binding.txtUpdate.text = convertDateTime(it, "yyyy-MM-dd'T'HH:mm:ss", "HH:mm:ss", Locale.KOREA) }
        binding.txtName.text = "${item.stationNo.replace("^0+".toRegex(), "")}. ${item.stationNm}"
        binding.txtParking.text = item.stationStatus.parkingCnt.toString()
        binding.txtRack.text = item.stationStatus.rackCnt.toString()
        binding.txtDistance.text = item.distance?.let { formatDistance(it) }

        binding.txtParking.setTextColor(
            when (item.stationStatus.parkingCnt) {
                0 -> ContextCompat.getColor(binding.root.context, R.color.red)
                in 1..3 -> ContextCompat.getColor(binding.root.context, R.color.yellow)
                else -> ContextCompat.getColor(binding.root.context, R.color.colorPrimary)
            }
        )
    }
}