package com.example.the_wheel_of_fortune

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluehomestudio.luckywheel.LuckyWheel
import com.bluehomestudio.luckywheel.OnLuckyWheelReachTheTarget
import com.bluehomestudio.luckywheel.WheelItem
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*


class MainActivity : AppCompatActivity(), OnKeyClick {
    lateinit var answer: String
    lateinit var adapter: KeypadAdapter
    lateinit var data: ArrayList<ItemsViewModel>
    lateinit var saveData: ArrayList<Char>
    lateinit var dialog: BottomSheetDialog
    lateinit var rec: RecyclerView
    var currentMoney: Int = 0
    var money: Int = 0
    var life: Int = 0
    lateinit var moneyText: TextView
    lateinit var lifeText: TextView
    lateinit var timer: TextView
    var isSolve = false
    var isVowel = false
    lateinit var cTimer:CountDownTimer
    var againPlay=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifeText = findViewById<TextView>(R.id.lifeText)
        timer = findViewById<TextView>(R.id.timer)
        moneyText = findViewById<TextView>(R.id.coinText)
        rec = findViewById<RecyclerView>(R.id.recyclerView)
        val question = findViewById<TextView>(R.id.questionText)
        val spinBtn = findViewById<Button>(R.id.spin)
        val solve = findViewById<Button>(R.id.solve)
        val vowel = findViewById<Button>(R.id.vowel)
        life = 5;
        money = 0;
        var intent=intent
        if(intent.getStringExtra("test")!=null){
            var saveMoney=getMoney();
            moneyText.text=saveMoney.toString()
        }
        data = ArrayList<ItemsViewModel>();
        startGame()
        saveData = ArrayList<Char>();
        val dataMap = ArrayList<QuestionList>()

        dataMap.add(QuestionList("Animal", "chicken"))
        dataMap.add(QuestionList("Country", "germany"))
        dataMap.add(QuestionList("Animal", "tyrannosaurus"))
        dataMap.add(QuestionList("Country", "denmark"))
        dataMap.add(QuestionList("Food", "spaghetti"))
        dataMap.add(QuestionList("Sport", "cricket"))
        dataMap.add(QuestionList("Person", "eriksen"))
        dataMap.add(QuestionList("Subject", "programming"))
        dataMap.add(QuestionList("Food", "cheeseburger"))

        val wheelItems: MutableList<WheelItem> = ArrayList()
        wheelItems.add(WheelItem(Color.rgb(120, 104, 0),
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
                "300"))
        wheelItems.add(WheelItem(Color.rgb(120, 104, 100),
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name), "Miss turn"))

        wheelItems.add(WheelItem(Color.BLUE,
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
                "100"))

        wheelItems.add(WheelItem(Color.RED,
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
                "200"))
        wheelItems.add(WheelItem(Color.BLUE,
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name), "Bankrupt"))

        wheelItems.add(WheelItem(Color.rgb(216, 0, 155),
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name), "500"))

        wheelItems.add(WheelItem(Color.rgb(120, 104, 100),
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
                "600"))

        wheelItems.add(WheelItem(Color.DKGRAY,
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
                "Extra turn"))

        val wheelSpinner = findViewById<LuckyWheel>(R.id.lwv);
        wheelSpinner.addWheelItems(wheelItems)
        val rand = Random()
        val randomNum: Int = rand.nextInt(dataMap.size - 1 - 0 + 1) + 0
        question.text = dataMap.get(randomNum).question;
        answer = dataMap.get(randomNum).answer.toString();

        rec.layoutManager = GridLayoutManager(this, 20)


        for (i in 1..5) {
            data.add(ItemsViewModel('!', false))
        }
        for (i in 0..answer.length - 1) {
            if (answer[i].toString().equals(" ")) {
                data.add(ItemsViewModel(answer[i], true))
            } else {
                data.add(ItemsViewModel(answer[i], false))
            }

        }
        var sizeDataList = data.size
        for (i in sizeDataList + 1..40) {
            data.add(ItemsViewModel('!', false))
        }

        for (i in 0..7) {
            var p = 0
            val randomNum2: Int = rand.nextInt(122 - 97 + 1) + 97
            for (i in 0..answer.length - 1) {
                if (answer[i].toString().equals(randomNum2.toChar().toString())) {
                    p = 1
                    break
                }
            }
            if (p == 0)
                saveData.add(randomNum2.toChar())


        }

        val adapter = CustomAdapter(data)
        rec.adapter = adapter


        spinBtn.setOnClickListener(View.OnClickListener { view ->
            isVowel = false
            val spinNum: Int = rand.nextInt(wheelItems.size - 1 - 0 + 1) + 0
            wheelSpinner.rotateWheelTo(spinNum + 1)
            wheelSpinner.setTarget(spinNum)
            wheelSpinner.setLuckyWheelReachTheTarget(OnLuckyWheelReachTheTarget {
                if (wheelItems.get(spinNum).text.toString().equals("Extra turn")) {
                    life += 1
                    lifeText.text = life.toString()
                    Toast.makeText(
                            this@MainActivity,
                            wheelItems.get(spinNum).text + " You earn one life",
                            Toast.LENGTH_LONG
                    ).show()
                } else if (wheelItems.get(spinNum).text.toString().equals("Miss turn")) {
                    life -= 1
                    lifeText.text = life.toString()
                    if (life == 0) {
                        endGame(false)
                    }
                    Toast.makeText(
                            this@MainActivity,
                            wheelItems.get(spinNum).text + " You loss one life",
                            Toast.LENGTH_LONG
                    ).show()

                } else if (wheelItems.get(spinNum).text.toString().equals("Bankrupt")) {
                    money = 0
                    moneyText.text = money.toString()
                    Toast.makeText(
                            this@MainActivity,
                            wheelItems.get(spinNum).text + " Now Total coin zero",
                            Toast.LENGTH_LONG
                    ).show()

                } else {

                    currentMoney = Integer.parseInt(wheelItems.get(spinNum).text.toString())
//                    money=money+Integer.parseInt(b)
//                    moneyText.text=money.toString()
                    showTimer(false);
                    showBottomSheet(false);
                    Toast.makeText(
                            this@MainActivity,
                            "Select Char for get " + wheelItems.get(spinNum).text + " Coin",
                            Toast.LENGTH_LONG
                    ).show()
                }

            })
        })
        vowel.setOnClickListener(View.OnClickListener { view ->
            isVowel = true
            isSolve = false
            if (money >= 250) {
                Toast.makeText(
                        this@MainActivity,
                        "Buy a vowel for 250 coin",
                        Toast.LENGTH_LONG
                ).show()
                showBottomSheet(true)
            } else {
                Toast.makeText(
                        this@MainActivity,
                        "Sorry, Your coin less than 250",
                        Toast.LENGTH_LONG
                ).show()
            }
        })
        solve.setOnClickListener(View.OnClickListener { view ->
            showTimer(true);
            isSolve = true
            isVowel = false
            showBottomSheet(false)
        })


    }

    fun showBottomSheet(isVowel: Boolean) {
        dialog = BottomSheetDialog(this, R.style.BottomSheetDialog)

        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)


        val recyclerView = view.findViewById<RecyclerView>(R.id.keyRec)
        recyclerView.layoutManager = GridLayoutManager(this, 12)
        val dataKeypad = ArrayList<ItemsViewModelKeypad>()
        if (isVowel) {
            dataKeypad.add(ItemsViewModelKeypad('a', false))
            dataKeypad.add(ItemsViewModelKeypad('e', false))
            dataKeypad.add(ItemsViewModelKeypad('i', false))
            dataKeypad.add(ItemsViewModelKeypad('o', false))
            dataKeypad.add(ItemsViewModelKeypad('u', false))
        } else {
            for (i in 97..122) {
                dataKeypad.add(ItemsViewModelKeypad(i.toChar(), false))
            }
        }
        adapter = KeypadAdapter(dataKeypad, this, saveData)
        recyclerView.adapter = adapter

        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }

    override fun onClick(item: ItemsViewModelKeypad) {
        var a = 0
        for (i in 0..answer.length - 1) {
            if (item.text.toString().equals(answer[i].toString())) {
                a = 1
                saveData.add(answer[i])
                data.set(i + 5, ItemsViewModel(answer[i], true))
                val adapter = CustomAdapter(data)
                rec.adapter = adapter

            }

        }
        if (isSolve) {
            if (a == 0) {
                life = life - 1
                lifeText.text = life.toString()
                if (life == 0) {
                    endGame(false)
                }
                Toast.makeText(
                        this@MainActivity,
                        "oops! You lose one life",
                        Toast.LENGTH_LONG
                ).show()
                cTimer.cancel()
                timer.text=""
                dialog.dismiss()
                return;
            }
            var z = 0
            for (i in 0..answer.length - 1) {
                if (data.get(i + 5).vis!!) {
                    z = z + 1
                } else {
                    z = 0
                }

            }
            if (z == answer.length) {
                endGame(true)
            }
        } else {
            if (a == 1) {
                if (isVowel) {
                    money = money - 250
                } else {
                    money = money + currentMoney
                }
                moneyText.text = money.toString()

            }
            if (a == 0) {
                if (isVowel) {
                    money = money - 250
                    moneyText.text = money.toString()
                }
                Toast.makeText(
                        this@MainActivity,
                        "oops!",
                        Toast.LENGTH_LONG
                ).show()
                life = life - 1
                lifeText.text = life.toString()
                if (life == 0) {
                    endGame(false)
                }
            }
            timer.text=""
            cTimer.cancel()
            dialog.dismiss()
        }

    }

    fun endGame(won: Boolean) {


        var inflater = this.getLayoutInflater();
        var view = inflater.inflate(R.layout.message_box, null, false);
        var builder = AlertDialog.Builder(this)
                .setTitle("").setView(view);

        var alert = builder.create();
        var imageView = view.findViewById<ImageView>(R.id.image1)
        var playAgain = view.findViewById<Button>(R.id.play_again)
        if (won) {
            saveData();
            imageView.setImageResource(R.drawable.win)
        }
        else
            imageView.setImageResource(R.drawable.lose)
        playAgain.setOnClickListener(View.OnClickListener { view ->
            againPlay=true
            val intent = intent
            intent.putExtra("test","1")
            finish()
            startActivity(intent)
        })

        alert.show()
        alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
    }

    fun startGame() {


        var inflater = this.getLayoutInflater();
        var view = inflater.inflate(R.layout.message_box, null, false);
        var builder = AlertDialog.Builder(this)
                .setTitle("").setView(view);

        var alert = builder.create();
        var imageView = view.findViewById<ImageView>(R.id.image1)
        var playAgain = view.findViewById<Button>(R.id.play_again)
        imageView.setImageResource(R.drawable.logo)

        playAgain.setOnClickListener(View.OnClickListener { view ->
            alert.dismiss()
        })
        playAgain.text = "Play"
        alert.show()
        alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
    }

    fun showTimer(isSolve: Boolean) {
         cTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timer.setText("Seconds remaining: " + millisUntilFinished / 1000)
            }
            override fun onFinish() {
                if(isSolve){
                    endGame(false)
                }
                else{
                    if(dialog!=null){
                        dialog.dismiss()
                    }
                }
                timer.setText("")
            }
        }
        cTimer.start()
       }

    override fun onDestroy() {
        if(!againPlay){
            saveDataEmpty()
        }
        super.onDestroy()
        cTimer.cancel()


    }
    fun saveData(){
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()
        myEdit.putString("coin", moneyText.text.toString())
        myEdit.commit()
    }
    fun saveDataEmpty(){
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        sharedPreferences.edit().remove("coin").commit();
    }
    @JvmName("getMoney1")
    fun getMoney():Int{
        val sh = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val s1 = sh.getString("coin", "")
        Log.d("test33",s1.toString())
        if (s1 != null) {
            if(s1.length>0){
                return Integer.parseInt(s1)
            }
        }
        return 0
    }



}