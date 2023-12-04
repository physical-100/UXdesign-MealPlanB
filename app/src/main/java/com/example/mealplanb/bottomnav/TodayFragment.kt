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
import androidx.core.app.ActivityCompat
import com.example.mealplanb.AllListNutrition
import com.example.mealplanb.R
import com.example.mealplanb.UserManager
import com.example.mealplanb.databinding.FragmentTodayBinding
import com.example.mealplanb.fragment.MealhomeFragment
import java.lang.Math.abs
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mealplanb.TodayWeight
import com.example.mealplanb.Totalcal
import com.example.mealplanb.fragment.MealDetailFragment

class TodayFragment : Fragment() {
    private lateinit var notificationManager: NotificationManagerCompat
    lateinit var binding: FragmentTodayBinding
    val userData = UserManager.getUserData()
    val userCal = UserManager.getUserCal()
    var totalkcal=0.0
    var todayWeight: TodayWeight?=null
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
        val isMealadd = arguments?.getString("식단 추가")
            Log.i("인자",isMealadd.toString())

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
        UserManager.setTotalcal(Totalcal(totalkcal,totalcarbo,totalproteion,totalfat))

        if (savedInstanceState != null) {
            scrollPosition = savedInstanceState.getInt("scroll_position", 0)
            scrollView.post { scrollView.scrollTo(0, scrollPosition) }
        }

        if (userCal != null) {
            // userCalory를 사용하여 필요한 작업 수행
            binding.apply {
                if(userCal.carb-totalcarbo>=0){
                    carbohydrate.text = "순탄수\n${String.format("%.1f",userCal.carb-totalcarbo)}g"

                }else{
                    carbohydrate.text= "순탄수\n${String.format("%.1f",abs(userCal.carb-totalcarbo))}g 초과"
                }

                if(userCal.protein-totalproteion>=0){
                    protein.text = "순탄수\n${String.format("%.1f",userCal.protein-totalproteion)}g"

                }else{
                    protein.text= "단백질\n${String.format("%.1f",abs(userCal.protein-totalproteion))}g 초과"
                }
                if(userCal.fat-totalfat>=0){
                    fat.text = "지방\n${String.format("%.1f",userCal.fat-totalfat)}g"
                }
                else{
                    fat.text= "지방\n${String.format("%.1f",abs(userCal.fat-totalfat))}g 초과"

                }
                if (userCal.goal_calory-totalkcal>=0){
                    leftoverCal.text= "${String.format("%d",(userCal.goal_calory-totalkcal).toInt())}kcal"

                }
                else{
                    leftoverCal.text="${String.format("%.1f",abs(userCal.goal_calory-totalkcal))}kcal"
                    textview2.text = "만큼 초과하여 드셨어요"

                }
                val usernameview=binding.nameDate
                usernameview?.text=userData?.username+"님의 1일차"


                val remainingCalories = (userCal.goal_calory - totalkcal).toFloat()

                calProgressBar.max=userCal.goal_calory

                calProgressBar.progress = if (remainingCalories >= 0) (userCal.goal_calory - remainingCalories).toInt() else userCal.goal_calory
            }


//            val argument = arguments?.getString("식단 추가")
//            Log.i("인자",argument.toString())
//            createAndShowNotification()
            Log.i("총 칼로리",totalkcal.toString())
            Log.i("목표 칼로리",userCal.goal_calory.toString())

        }
        if(isMealadd!=null) {
            //식단이 추가 되었을 때 알림 호출
            notificationManager = NotificationManagerCompat.from(requireContext())
            createAndShowNotification()
            updateNotification(totalkcal,userCal!!.goal_calory)
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

            todayWeight=UserManager.getUserTodayWeight()
            val dailyWeightFragment =
                childFragmentManager.findFragmentById(R.id.addweight) as? DailyweightFragment
            dailyWeightFragment?.updateWeightText(todayWeight!!.weight)
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
            .setContentText("다이어트가 장난입니까..? ")
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
            return
        }
//        notificationManager.notify(1, builder.build())
    }
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
                notificationLayout.setTextViewText(R.id.notification_title, "벌써 30퍼센트 이상 하셨군요!!")
                notificationLayout.setTextViewText(R.id.notification_text, "자신을 믿어요!")
            }
            else -> {
                // Do nothing for other percentages
                notificationLayout.setTextViewText(R.id.notification_title, "활기찬 식단 기록을 해보아요!!")
                notificationLayout.setTextViewText(R.id.notification_text, "자신을 믿어요!")
                return
            }
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        notificationManager.notify(1, customNotification)
    }
}


