package com.app.gradenwateringscheduler.ui.home.fragment

import Plant
import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.gradenwateringscheduler.R
import com.app.gradenwateringscheduler.databinding.FragmentAddPlantViewBinding
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddPlantViewFragment : Fragment() {

    private lateinit var binding: FragmentAddPlantViewBinding
    private val selectedDays = mutableSetOf("Mon", "Wed", "Fri")
    private val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

    private var selectedBitmap: Bitmap? = null
    private var selectedImageUri: Uri? = null



    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val bitmap = result.data?.extras?.get("data") as? Bitmap
            if (bitmap != null) {
                selectedBitmap = bitmap
                selectedImageUri = null
                binding.plantImageView.setImageBitmap(bitmap)
                binding.plantImageView.visibility = View.VISIBLE
                binding.placeholderContainer.visibility = View.GONE
            }
        }
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                selectedBitmap = null
                selectedImageUri = uri
                binding.plantImageView.setImageURI(uri)
                binding.plantImageView.visibility = View.VISIBLE
                binding.placeholderContainer.visibility = View.GONE
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddPlantViewBinding.inflate(inflater, container, false)
        val db = AppDatabase.getDatabase(requireContext())

        binding.addPlantToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.selectWateringDaysText.visibility = View.GONE
        binding.selectWateringDays.visibility = View.GONE

        binding.wateringSchedulerType.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Switch is ON (Daily)
                binding.selectWateringDaysText.visibility = View.GONE
                binding.selectWateringDays.visibility = View.GONE
            } else {
                // Switch is OFF (Custom)
                binding.selectWateringDaysText.visibility = View.VISIBLE
                binding.selectWateringDays.visibility = View.VISIBLE            }
        }


        binding.camera.setOnClickListener { openCamera() }
        binding.gallery.setOnClickListener { openGallery() }

        val dayMap = mapOf(
            "Sun" to binding.daySun.dayTextView,
            "Mon" to binding.dayMon.dayTextView,
            "Tue" to binding.dayTue.dayTextView,
            "Wed" to binding.dayWed.dayTextView,
            "Thu" to binding.dayThu.dayTextView,
            "Fri" to binding.dayFri.dayTextView,
            "Sat" to binding.daySat.dayTextView
        )

        dayMap.forEach { (day, tv) ->
            tv.text = day
            tv.setOnClickListener {
                if (selectedDays.contains(day)) selectedDays.remove(day) else selectedDays.add(day)
                updateDayUI(dayMap)
            }
        }

        updateDayUI(dayMap)
        addTimeField()

        binding.addTimeButton.setOnClickListener { addTimeField() }

        binding.addPlantButton.setOnClickListener {
            val plantName = binding.plantName.text.toString().trim()
            val type = binding.plantType.text.toString().trim()
            val scheduleType = if (binding.wateringSchedulerType.isChecked) "daily" else "custom"
            val daysString = selectedDays.joinToString(",")
            val timeList = mutableListOf<String>()

            for (i in 0 until binding.layoutTimeFieldsContainer.childCount) {
                val container = binding.layoutTimeFieldsContainer.getChildAt(i) as LinearLayout
                val tv = container.getChildAt(0) as TextView
                timeList.add(tv.text.toString())
            }

            if (plantName.isEmpty() || type.isEmpty() || (selectedBitmap == null && selectedImageUri == null)) {
                Toast.makeText(requireContext(), "Please complete all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val savedUri = saveSelectedImage()
                if (savedUri == null) {
                    Toast.makeText(requireContext(), "Failed to save image", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val plant = Plant(
                    name = plantName,
                    type = type,
                    scheduleType = scheduleType,
                    days = daysString,
                    times = timeList.joinToString(","),
                    photoUri = savedUri.toString()
                )

                db.plantDao().insertPlant(plant)
                Toast.makeText(requireContext(), "Plant saved", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }

        return binding.root
    }

    private fun updateDayUI(dayMap: Map<String, TextView>) {
        dayMap.forEach { (day, tv) ->
            if (selectedDays.contains(day)) {
                tv.setBackgroundResource(R.drawable.circle_selected)
                tv.setTextColor(Color.WHITE)
            } else {
                tv.setBackgroundResource(R.drawable.circle_unselected)
                tv.setTextColor(Color.BLACK)
            }
        }
    }

    private fun addTimeField() {
        val context = requireContext()
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = 16 }
        }

        val timeTV = TextView(context).apply {
            text = timeFormat.format(Calendar.getInstance().time)
            setTextColor(ContextCompat.getColor(context, R.color.black))
            textSize = 16f
            background = ContextCompat.getDrawable(context, R.drawable.border_textview)
            setPadding(32, 24, 32, 24)
            isClickable = true
            isFocusable = true
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            setOnClickListener { openTimePicker(this) }
        }

        val deleteBtn = ImageButton(context).apply {
            setImageResource(android.R.drawable.ic_menu_delete)
            setBackgroundColor(Color.TRANSPARENT)
            setColorFilter(Color.RED)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { leftMargin = 16 }
            setOnClickListener {
                binding.layoutTimeFieldsContainer.removeView(container)
                updateDeleteButtonsVisibility()
            }
        }

        container.addView(timeTV)
        container.addView(deleteBtn)
        binding.layoutTimeFieldsContainer.addView(container)

        updateDeleteButtonsVisibility()
        binding.scrollViewTimeFields.post { binding.scrollViewTimeFields.fullScroll(View.FOCUS_DOWN) }
    }

    private fun updateDeleteButtonsVisibility() {
        val count = binding.layoutTimeFieldsContainer.childCount
        for (i in 0 until count) {
            val container = binding.layoutTimeFieldsContainer.getChildAt(i) as LinearLayout
            val deleteButton = container.getChildAt(1) as ImageButton
            deleteButton.visibility = if (count > 1) View.VISIBLE else View.GONE
        }
    }

    private fun openTimePicker(textView: TextView) {
        val calendar = Calendar.getInstance()
        val timePicker = TimePickerDialog(
            requireContext(),
            { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                textView.text = timeFormat.format(calendar.time)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )
        timePicker.show()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private suspend fun saveSelectedImage(): Uri? {
        return try {
            if (selectedBitmap != null) {
                val filename = "plant_${System.currentTimeMillis()}.png"
                val file = File(requireContext().filesDir, filename)
                file.outputStream().use { out ->
                    selectedBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, out)
                }
                Uri.fromFile(file)
            } else if (selectedImageUri != null) {
                val inputStream = requireContext().contentResolver.openInputStream(selectedImageUri!!)
                val filename = "plant_${System.currentTimeMillis()}.png"
                val file = File(requireContext().filesDir, filename)
                inputStream.use { input ->
                    file.outputStream().use { output ->
                        input?.copyTo(output)
                    }
                }
                Uri.fromFile(file)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
