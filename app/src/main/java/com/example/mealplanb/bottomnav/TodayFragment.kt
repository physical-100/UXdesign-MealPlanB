package com.example.mealplanb.bottomnav

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RemoteViews
import android.widget.ScrollView
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.example.mealplanb.AllListNutrition
import com.example.mealplanb.R
import com.example.mealplanb.UserManager
import com.example.mealplanb.User_calory
import com.example.mealplanb.databinding.FragmentTodayBinding
import com.example.mealplanb.fragment.MealhomeFragment
import java.lang.Math.abs
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class TodayFragment : Fragment() {
    private lateinit var notificationManager: NotificationManagerCompat
    lateinit var binding: FragmentTodayBinding
    val userData = UserManager.getUserData()
    val userCal = UserManager.getUserCal()
    private lateinit var scrollView: ScrollView
    private var scrollPosition = 0
    private var AllListNutritionList= arrayListOf<AllListNutrition>(
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // ScrollView의 스크롤 위치 저장
        scrollPosition = scrollView.scrollY
        outState.putInt("scroll_position", scrollPosition)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTodayBinding.inflate(inflater, container, false)
        scrollView =binding.scrollView
        // 알림 띄우기 위한 설정  오늘의 목표치 30%,60%,80%에서 알람을 보냄

        // 스크롤 위치를 복원
        AllListNutritionList=UserManager.getAllListNutritionList()
        var totalcarbo=0.0
        var totalproteion=0.0
        var totalfat=0.0
        var totalkcal=0.0
        for(eachNutrtion in AllListNutritionList){
            totalcarbo+=eachNutrtion.carbo
            totalproteion+=eachNutrtion.protein
            totalfat+=eachNutrtion.fat
            totalkcal+=eachNutrtion.kcal
        }

        if (savedInstanceState != null) {
            scrollPosition = savedInstanceState.getInt("scroll_position", 0)
            scrollView.post { scrollView.scrollTo(0, scrollPosition) }
        }
//        val userCalory = arguments?.getParcelable<User_calory>("user_calory")
//        if (userCalory != null) {
//            // userCalory를 사용하여 필요한 작업 수행
//            binding.apply {
//                goalCarb.text = "/${userCalory.carb}g"
//                goalProtein.text = "/${userCalory.protein}g"
//                goalFat.text = "/${userCalory.fat}g"
//                goal.text= "${userCalory.goal_calory} Kcal"
//
//            }

        if (userCal != null) {
            // userCalory를 사용하여 필요한 작업 수행
            binding.apply {
                if(userCal.carb-totalcarbo>=0){
                    carbohydrate.text = "순탄수\n${String.format("%.1f",userCal.carb-totalcarbo)}g"

                }else{
                    carbohydrate.text= "순단백질${String.format("%.1f",abs(userCal.carb-totalcarbo))}g만큼 초과하여 드셨어요"
                }

                if(userCal.protein-totalproteion>=0){
                    protein.text = "순탄수\n${String.format("%.1f",userCal.protein-totalproteion)}g"

                }else{
                    protein.text= "순단백질${String.format("%.1f",abs(userCal.protein-totalproteion))}g만큼 초과하여 드셨어요"
                }
                if(userCal.fat-totalfat>=0){
                    fat.text = "지방\n${String.format("%.1f",userCal.fat-totalfat)}g"
                }
                else{
                    fat.text= "순지방${String.format("%.1f",abs(userCal.fat-totalfat))}g만큼 초과하여 드셨어요"

                }
                if (userCal.goal_calory-totalkcal>=0){
                    leftoverCal.text= "오늘은 ${String.format("%.1f",userCal.goal_calory-totalkcal)}kcal 남았어요"

                }
                else{
                    leftoverCal.text="목표열량에서 ${String.format("%.1f",abs(userCal.goal_calory-totalkcal))}kcal만큼 초과하여 드셨어요"

                }
                val remainingCalories = (userCal.goal_calory - totalkcal).toFloat()

                calProgressBar.max=userCal.goal_calory

                calProgressBar.progress = if (remainingCalories >= 0) (userCal.goal_calory - remainingCalories).toInt() else userCal.goal_calory
            }
            notificationManager = NotificationManagerCompat.from(requireContext())
//            createAndShowNotification()
            Log.i("총 칼로리",totalkcal.toString())
            Log.i("목표 칼로리",userCal.goal_calory.toString())
            updateNotification(totalkcal,userCal.goal_calory)
        }
        val mealhomeFragment = MealhomeFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.addmeal, mealhomeFragment)
            .commit()

        val dailyweightFragment = DailyweightFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.addweight, dailyweightFragment)
            .commit()

    return  binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Disable back press
        binding.scrollView.setOnClickListener {
            scrollPosition = binding.scrollView.scrollY
            // Perform your screen transition here...
        }
    }
    override fun onResume() {
        super.onResume()
        // Restore the scroll position when the fragment is resumed
        binding.scrollView.post() { binding.scrollView.scrollTo(0, scrollPosition) }
    }

    // 알림띄우는 코드
    private fun createAndShowNotification() {
        // 알림 생성
        val channelId = "your_channel_id"
        val builder = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.character)
            .setContentTitle("두번째 식단을 입력하세요")
            .setContentText("다이어트가 장난입니까..? 어서 식단을 기록해 시발년아")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        // 알림 띄우기
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(1, builder.build())
    }
    // Remove this function
// private fun NotificationCompat.Builder.setCustomBigContentView(notificationLayout: Int) {}

    // Update your updateNotification function
    private fun updateNotification(totalCalories: Double, goalCalories: Int) {
        val channelId = "your_channel_id"
        val notificationLayout = RemoteViews(requireContext().packageName, R.layout.notification_layout)

        // Apply the layouts to the notification
        val customNotification = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.character)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationLayout)
            .build()

        // Calculate the percentage completion
        val percentage = (totalCalories / goalCalories) * 100
        Log.i("퍼센트", percentage.toString())

        // Different notifications based on the percentage
        when {
            percentage > 103 -> {
                notificationLayout.setImageViewResource(R.id.notification_image, R.drawable.character)
                notificationLayout.setTextViewText(R.id.notification_title, "그만좀 먹어라!")
                notificationLayout.setTextViewText(R.id.notification_text, "제발!!")
            }
            percentage >= 100 -> {
                notificationLayout.setTextViewText(R.id.notification_title, "목표 달성 축하해요!!")
                notificationLayout.setTextViewText(R.id.notification_text, "내일도 열심히 해봅시다!!!")
            }
            percentage > 80 -> {
                notificationLayout.setTextViewText(R.id.notification_title, "거의 다왔어요!!")
                notificationLayout.setTextViewText(R.id.notification_text, "화이팅!")
            }
            percentage > 60 -> {
                notificationLayout.setTextViewText(R.id.notification_title, "오늘 하루도 열심히 가봅시다 !!")
                notificationLayout.setTextViewText(R.id.notification_text, "힘내세요!")
            }
            percentage > 30 -> {
                notificationLayout.setTextViewText(R.id.notification_title, "활기찬 식단 기록을 해보아요!!")
                notificationLayout.setTextViewText(R.id.notification_text, "자신을 믿어요!")
            }
            else -> {
                // Do nothing for other percentages
                return
            }
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Handle the case where permissions are not granted
            return
        }

        notificationManager.notify(1, customNotification)
    }





}

private fun NotificationCompat.Builder.setCustomBigContentView(notificationLayout: Int) {

}

