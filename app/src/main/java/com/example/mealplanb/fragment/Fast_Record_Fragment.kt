package com.example.mealplanb.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplanb.adapter.FastrecordAdapter
import com.example.mealplanb.databinding.FragmentFastRecordBinding
import com.example.mealplanb.dataclass.fast_record
import com.example.mealplanb.dataclass.food

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Fast_Record_Fragment : Fragment() {
    lateinit var binding:FragmentFastRecordBinding
    private var param1: String? = null
    private var param2: String? = null
    val fastrecord = arrayListOf(
    fast_record("12시간 00분", "11월 24일 오후 2:18", "11월 25일 오전 2:18"),
    fast_record("11시간 00분", "11월 26일 오후 2:18", "11월 27일 오전 1:18")
    )
    lateinit var adapter: FastrecordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFastRecordBinding.inflate(inflater,container,false)
        Log.i("데이터",fastrecord.toString())
        initRecyclerview()
        return binding.root
    }
    private fun initRecyclerview(){
            binding.fastRecord.layoutManager = LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL,false)

            adapter = FastrecordAdapter(fastrecord)

            binding.fastRecord.adapter = adapter

            //음식목록 클릭시 어디 프래그먼ㅈㅈ트로 갈지 정하는 코드
            adapter!!.itemClickListener = object : FastrecordAdapter.OnItemClickListener{
                override fun OnItemClick(data: fast_record, holder: FastrecordAdapter.ViewHolder) {

                }

            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fast_Record_Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fast_Record_Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}