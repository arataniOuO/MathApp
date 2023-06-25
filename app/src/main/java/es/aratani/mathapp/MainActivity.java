package es.aratani.mathapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import es.aratani.mathapp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class MainActivity extends AppCompatActivity {

    MathApp ma = new MathApp();
    int maxNum, comboNum;

    float resultNum;
    TextView operation, combo;
    EditText answer;
    Button check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        operation = findViewById(R.id.operation);
        answer = findViewById(R.id.answer);
        check = findViewById(R.id.check);
        combo = findViewById(R.id.combo);

        answer.setImeOptions(EditorInfo.IME_ACTION_DONE);
        answer.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        answer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    Check();
                    return true;
                }
                return false;
            }

        });


        Init();
    }


    void Init(){
        UpdateCombo(false);
        ChangeDifficulty(0);
        GenerateOperation();
    }

    //region Math
    void GenerateOperation(){
        int num1 = ma.GenerateRandom(maxNum);
        int opType = ma.GenerateRandom(4);
        int num2 = ma.GenerateRandom(maxNum);

        if(opType == 3){
            while(num2 == 0)
                num2 = ma.GenerateRandom(maxNum);
        }



        operation.setText(ma.GenerateOpString(num1, num2, opType));

        resultNum = ma.GetOperation(num1, num2, opType);
        check.setVisibility(View.VISIBLE);
        check.setEnabled(true);
    }

    void GenerateOperation(float lastResult){
        int num2 = ma.GenerateRandom(maxNum);
        int opType = ma.GenerateRandom(4);

        operation.setText(ma.GenerateOpString(lastResult, num2, opType));

        resultNum = ma.GetOperation(resultNum, num2, opType);
    }
    //endregion

    //region Answer
    boolean CheckAnswer(){
        float userAnswer = 0;
        try{
            String userInput = answer.getText().toString();
            userInput = userInput.replace(',', '.');
            userAnswer = Float.parseFloat(userInput);
            System.out.println(userAnswer);
        }catch (NumberFormatException e){
            System.out.println(e);
        }

        if(resultNum % 1 == 0){
            return resultNum == userAnswer;
        }else{
            DecimalFormat df = new DecimalFormat("#.#", new DecimalFormatSymbols(Locale.US));
            float formattedOperationResult = Float.parseFloat(df.format(resultNum).replace(',', '.'));
            float formattedUserInput = Float.parseFloat(df.format(userAnswer).replace(',', '.'));
            return formattedUserInput == formattedOperationResult;
        }
    }

    void FormatAnswer(boolean answer){
        if(answer){
            ClearInput();
            GenerateOperation(resultNum);

            operation.setTextColor(Color.BLACK);
        }else{
            ClearInput();


            check.setEnabled(false);
            check.setVisibility(View.INVISIBLE);
            operation.setTextColor(Color.RED);
        }

        UpdateCombo(answer);
    }

    void ClearInput(){
        answer.setText("");
    }
    //endregion

    //region Difficulty
    void ChangeDifficulty(int difficulty){
        switch (difficulty){
            case 1:
                maxNum = 50;
                break;
            case 2:
                maxNum = 101;
                break;
            default:
                maxNum = 11;

        }
    }
    //endregion

    //region Combo

    void UpdateCombo(boolean answer){
        if(answer) comboNum += 1;
        else comboNum = 0;

        combo.setText("Current combo: x" + comboNum);
    }

    //endregion

    //region ButtonsFunctions
    public void Check(View view){
        FormatAnswer(CheckAnswer());
    }

    public void Check(){
        FormatAnswer(CheckAnswer());
    }

    public void Refresh(View view){
        ClearInput();
        GenerateOperation();

        operation.setTextColor(Color.BLACK);
    }

    public void SetDifficulty(View view){
        operation.setTextColor(Color.BLACK);
        ChangeDifficulty(Integer.parseInt(view.getTag().toString()));
        GenerateOperation();
    }
    //endregion







}