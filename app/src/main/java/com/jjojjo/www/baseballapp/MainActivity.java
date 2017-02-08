package com.jjojjo.www.baseballapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    NumberPicker firstNumPic, secondNumPic, thirdNumPic;
    int firstPickedNum, secondPickedNum, thirdPickedNum;
    int arrayListCnt, flag;
    Button btnConfirm;
    ListView txtResult;
    ArrayList<String> result = new ArrayList<>();
    BaseballGame bg = new BaseballGame();
    int[] com;
    String gameResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetPickedNumber getPickedNumber = new GetPickedNumber();

        firstNumPic = (NumberPicker)findViewById(R.id.firstNumPic);
        secondNumPic = (NumberPicker)findViewById(R.id.secondNumPic);
        thirdNumPic = (NumberPicker)findViewById(R.id.thirdNumPic);

        btnConfirm = (Button)findViewById(R.id.btnConfirm);

        txtResult = (ListView)findViewById(R.id.ltResult);

        firstNumPic.setMinValue(0);
        firstNumPic.setMaxValue(9);
        secondNumPic.setMinValue(0);
        secondNumPic.setMaxValue(9);
        thirdNumPic.setMinValue(0);
        thirdNumPic.setMaxValue(9);

        firstNumPic.setOnValueChangedListener(getPickedNumber);
        secondNumPic.setOnValueChangedListener(getPickedNumber);
        thirdNumPic.setOnValueChangedListener(getPickedNumber);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag == 18){
                    com = null;
                    flag = 0;
                }
                gameResult = bg.baseballGameGo(firstPickedNum, secondPickedNum, thirdPickedNum, v, com);

                result.add(arrayListCnt, gameResult);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.listview, result);
                txtResult.setAdapter(adapter);
            }
        });
    }

    class GetPickedNumber implements NumberPicker.OnValueChangeListener {

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            if(picker.getId() == R.id.firstNumPic){
                firstPickedNum = firstNumPic.getValue();
            }
            else if(picker.getId() == R.id.secondNumPic){
                secondPickedNum = secondNumPic.getValue();
            }
            else if(picker.getId() == R.id.thirdNumPic){
                thirdPickedNum = thirdNumPic.getValue();
            }
        }
    }

    class BaseballGame {

        public String baseballGameGo(int firstPickedNum, int secondPickedNum, int thirdPickedNum, View v, int[] comR) {
            // TODO 숫자 야구 게임

            if(com == null) {
                com = new int[3];
                Random random = new Random();

                for (int i = 0; i < 3; i++) {                      // 랜덤으로 숫자 생성하여 배열에 넣기
                    com[i] = random.nextInt(9) + 1;            // 같은 숫자가 없을 때까지 반복
                    for (int j = i - 1; j >= 0; j--) {
                        if (com[i] == com[j]) {
                            i--;
                        }
                    }
                }
                return "시작하자!!!";
            }
            else{

                com = comR;

                int user[] = new int[3];                      // 사용자가 입력한 숫자 배열에 넣기

                int strike = 0, ball = 0, gameEnd = 0;

                for (; ; ) {
                    strike = 0;       // 값이 계속 누적 됨. 초기화 해 줌.
                    ball = 0;

                    gameEnd++;

                    user[0] = firstPickedNum;        // 100으로 나눈 몫
                    user[1] = secondPickedNum;     // 100으로 나눈 나머지를 10으로 나눈 몫
                    user[2] = thirdPickedNum;     // 100으로 나눈 나머지를 10으로 나눈 나머지

                    for (int i = 0; i < 3; i++) {           // 스트라익?
                        for (int j = 0; j < 3; j++) {
                            if (i == j) {
                                if (com[i] == user[j]) {
                                    strike++;
                                }
                            } else {                     // 볼
                                if (com[i] == user[j]) {
                                    ball++;
                                }
                            }
                        }
                    }

                    if (strike == 3) {                               // 다 맞음
                        flag = 18;
                        return "당신은 천재임 !! 정답 : " + com[0] + " " + com[1] + " " + com [2];
                    } else if (strike == 0 && ball == 0) {                  // 하나도 안맞음
                        return "아웃 : " + user[0] + " " + user[1] + " " + user[2];
                    } else {                                            // 일부 맞음
                        return strike + " 스트라이크 " + ball + " 볼 : " + user[0] + " " + user[1] + " " + user[2];
                    }
                }
            }
        }
    }
}
