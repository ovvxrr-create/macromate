package com.macromate.tracker;
import android.app.Activity;
import android.os.Bundle;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;

import android.view.Gravity;
import android.view.View;
import android.widget.*;
import android.text.InputType;
import org.json.*;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {
 final String BASE="https://eltccumtmczukzilsdvo.supabase.co",KEY="sb_publishable_InNmI8zaEx0l9OG41AXrsQ_bGhc71I1";
 SharedPreferences prefs; LinearLayout root,content; TextView title; String meal="Lunch",email="",token="";
 int purple=Color.rgb(167,80,243),white=Color.WHITE,muted=Color.rgb(185,174,204),surface=Color.rgb(31,26,44);
 java.util.concurrent.ExecutorService io=Executors.newSingleThreadExecutor();
 @Override public void onCreate(Bundle b){super.onCreate(b);getWindow().setStatusBarColor(Color.rgb(12,11,23));getWindow().setNavigationBarColor(Color.rgb(12,11,23));prefs=getSharedPreferences("macromate_native",MODE_PRIVATE);token=prefs.getString("token","");home();}
 TextView text(String s,int size){TextView t=new TextView(this);t.setText(s);t.setTextColor(white);t.setTextSize(size);t.setPadding(12,12,12,12);return t;}
 LinearLayout column(){LinearLayout l=new LinearLayout(this);l.setOrientation(1);l.setPadding(20,16,20,16);return l;}
 void page(String heading){root=column();root.setBackgroundColor(Color.rgb(12,11,23));ScrollView sc=new ScrollView(this);sc.setFillViewport(true);sc.addView(root);setContentView(sc);TextView brand=text("✦  MacroMate",24);brand.setTextColor(Color.rgb(203,131,255));brand.setTypeface(null,Typeface.BOLD);root.addView(brand);title=text(heading,23);root.addView(title);content=column();root.addView(content);}
 void line(String s){TextView t=text(s,16);t.setBackgroundColor(surface);LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.setMargins(0,5,0,5);content.addView(t,p);}
 void button(String s,Runnable click){Button b=new Button(this);b.setText(s);b.setAllCaps(false);b.setTextColor(white);b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(purple));content.addView(b);b.setOnClickListener(v->click.run());}
 EditText input(String hint,boolean numeric){EditText e=new EditText(this);e.setSingleLine(true);e.setHint(hint);e.setHintTextColor(muted);e.setTextColor(white);if(numeric)e.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);content.addView(e);return e;}
 double value(String k){return Double.longBitsToDouble(prefs.getLong(k,Double.doubleToLongBits(0)));}
 void set(String k,double n){prefs.edit().putLong(k,Double.doubleToLongBits(n)).apply();}
 String day(){return new java.text.SimpleDateFormat("yyyy-MM-dd",java.util.Locale.US).format(new java.util.Date());}
 String key(String k){return day()+"_"+k;}
 void home(){page("Your daily dashboard");double cal=value(key("cal"));line("✦  "+Math.round(cal)+" kcal eaten today");line("🥩 Protein  "+Math.round(value(key("protein")))+" g");line("🍞 Carbs  "+Math.round(value(key("carbs")))+" g");line("🥑 Fat  "+Math.round(value(key("fat")))+" g");line("💧 Water  "+Math.round(value(key("water")))+" ml");button("🍴 Add Food",()->foods());button("💧 +250 ml",()->{set(key("water"),value(key("water"))+250);home();});button("📈 Progress",()->progress());button("🏋 Gym exercise library",()->gym());line("Core tracking works offline. Your data stays on this device unless you export it.");}
 void foods(){page("Add food");line("Choose a food and enter grams. Nutrition is approximate per 100 g.");String[] names={"Chicken breast cooked","White rice cooked","Egg whole","Oats dry","Banana","Apple","Salmon cooked","Milk whole","Potato boiled","Bread white","Greek yogurt","Tuna canned","Almonds","Olive oil","Lentils cooked","Dates"};double[][] v={{165,31,0,3.6},{130,2.7,28.2,.3},{143,12.6,.7,9.5},{389,16.9,66.3,6.9},{89,1.1,22.8,.3},{52,.3,13.8,.2},{206,22.1,0,12.4},{61,3.2,4.8,3.3},{87,1.9,20.1,.1},{265,9,49,3.2},{97,9,3.9,5},{116,25.5,0,.8},{579,21.2,21.6,49.9},{884,0,0,100},{116,9,20,.4},{282,2.5,75,.4}};for(int i=0;i<names.length;i++){final int n=i;button(names[i]+" · "+(int)v[i][0]+" kcal/100g",()->add(names[n],v[n]));}button("✎ Describe custom food",()->custom());button("← Dashboard",()->home());}
 void add(String name,double[] vals){page(name);EditText grams=input("Amount in grams (e.g. 150)",true);button("Log food",()->{try{double amount=Double.parseDouble(grams.getText().toString())/100;if(amount<=0||amount>100)return;String[] fields={"cal","protein","carbs","fat"};for(int i=0;i<4;i++)set(key(fields[i]),value(key(fields[i]))+vals[i]*amount);Toast.makeText(this,"Food saved",Toast.LENGTH_SHORT).show();home();}catch(Exception e){Toast.makeText(this,"Enter valid grams",Toast.LENGTH_SHORT).show();}});}
 void custom(){page("Describe your food");EditText name=input("Description (e.g. homemade chicken wrap)",false),kcal=input("Calories",true),p=input("Protein grams",true),c=input("Carbs grams",true),f=input("Fat grams",true);button("Save custom food",()->{try{if(name.getText().length()==0)return;EditText[] es={kcal,p,c,f};String[] fields={"cal","protein","carbs","fat"};for(int i=0;i<4;i++){double n=Double.parseDouble(es[i].getText().toString());if(n<0||n>20000)throw new Exception();set(key(fields[i]),value(key(fields[i]))+n);}home();}catch(Exception e){Toast.makeText(this,"Enter valid nutrition values",Toast.LENGTH_SHORT).show();}});}
 void progress(){page("Progress");line("Today's calories: "+Math.round(value(key("cal"))));line("Today's water: "+Math.round(value(key("water")))+" ml");EditText weight=input("Weight (kg)",true);button("Save weight",()->{try{set("weight",Double.parseDouble(weight.getText().toString()));progress();}catch(Exception e){}});line("Latest weight: "+Math.round(value("weight"))+" kg");button("← Dashboard",()->home());}

 String[][] EXERCISES={
 {"Chest","Push-up","Place hands slightly wider than shoulders, brace your core, lower chest between hands, press up.","Keep hips aligned; exhale as you press."},
 {"Chest","Bench press","Lie with eyes below bar, plant feet, lower bar to mid-chest, press up over shoulders.","Use a spotter and safety arms; never bounce the bar."},
 {"Chest","Incline dumbbell press","Set bench to 15–30°, keep wrists stacked, lower elbows slightly below shoulders, press up.","Avoid flaring elbows excessively."},
 {"Chest","Dumbbell fly","Lie on bench with soft elbows, open arms in a wide arc, stop at comfortable stretch, bring hands together.","Use light weights; do not overstretch."},
 {"Chest","Cable chest fly","Set pulleys near shoulder height, step forward, draw hands together in front of chest, return slowly.","Keep shoulders down and elbows softly bent."},
 {"Chest","Chest dip","Support body on parallel bars, lean slightly forward, bend elbows as tolerated, press back up.","Avoid deep ranges if shoulders hurt."},
 {"Back","Lat pulldown","Grip bar just outside shoulder width, pull elbows toward ribs, bring bar to upper chest, return slowly.","Do not pull behind the neck or swing."},
 {"Back","Pull-up","Hang with active shoulders, drive elbows down, lift chin toward bar, lower under control.","Use assistance if needed; avoid kipping."},
 {"Back","Seated cable row","Sit tall, reach forward without rounding low back, pull handle toward lower ribs, release slowly.","Avoid leaning backward to cheat."},
 {"Back","One-arm dumbbell row","Support hand and knee on bench, keep back neutral, pull elbow toward hip, lower slowly.","Keep torso steady."},
 {"Back","Barbell bent-over row","Hinge at hips with soft knees, brace torso, row bar toward lower ribs, lower under control.","Keep neutral spine; start light."},
 {"Back","Chest-supported row","Lie chest-down on inclined bench, pull dumbbells toward ribs, pause, lower slowly.","Keep chest against pad."},
 {"Back","Straight-arm pulldown","Stand at cable stack, arms nearly straight, pull bar toward thighs using lats, return slowly.","Avoid bending elbows into a triceps pushdown."},
 {"Shoulders","Overhead press","Brace glutes and abs, start bar near collarbone, press overhead, lower under control.","Avoid excessive lower-back arch."},
 {"Shoulders","Dumbbell shoulder press","Sit or stand tall, start dumbbells at ear level, press upward, lower slowly.","Keep wrists stacked over elbows."},
 {"Shoulders","Lateral raise","With slight elbow bend, raise dumbbells out to sides to shoulder level, lower slowly.","Use light weights and avoid shrugging."},
 {"Shoulders","Rear delt fly","Hinge forward with neutral back, open arms sideways, squeeze rear shoulders, lower slowly.","Do not swing torso."},
 {"Shoulders","Face pull","Set rope at face height, pull toward forehead with elbows wide, rotate hands beside ears.","Keep neck relaxed."},
 {"Shoulders","Front raise","Raise light dumbbells in front to shoulder level, lower slowly.","Avoid leaning backward."},
 {"Biceps","Dumbbell curl","Stand tall, keep elbows near sides, curl dumbbells toward shoulders, lower slowly.","Do not swing hips."},
 {"Biceps","Hammer curl","Keep palms facing inward, curl with elbows fixed near torso, lower slowly.","Keep wrists straight."},
 {"Biceps","Preacher curl","Support upper arms on pad, curl through comfortable range, lower without locking hard.","Use moderate loads."},
 {"Biceps","Cable curl","Stand facing low cable, keep elbows by sides, curl handle toward shoulders, return slowly.","Avoid shrugging."},
 {"Triceps","Cable pushdown","Keep elbows pinned to sides, extend forearms down, squeeze triceps, return slowly.","Do not use body momentum."},
 {"Triceps","Overhead triceps extension","Hold weight overhead, bend elbows to lower behind head, straighten arms.","Keep elbows pointing forward."},
 {"Triceps","Close-grip push-up","Set hands slightly narrower than shoulders, lower with elbows near ribs, press up.","Keep wrists comfortable."},
 {"Triceps","Skull crusher","Lie on bench, hold weights over shoulders, bend elbows to lower near head, extend.","Start light; keep upper arms controlled."},
 {"Legs","Bodyweight squat","Stand feet shoulder-width, sit hips down between knees, keep heels grounded, stand tall.","Track knees over toes; use pain-free depth."},
 {"Legs","Goblet squat","Hold dumbbell at chest, brace, squat between knees, drive through midfoot to stand.","Do not force depth."},
 {"Legs","Barbell back squat","Set bar on upper back, brace, sit down between hips, stand with controlled drive.","Use safeties and suitable coaching."},
 {"Legs","Leg press","Sit with back supported, lower sled until comfortable knee bend, press through whole foot.","Never let lower back round or lock knees hard."},
 {"Legs","Romanian deadlift","Hold weight near thighs, soften knees, hinge hips back with neutral spine, stand by driving hips forward.","Keep weight close; avoid rounding."},
 {"Legs","Conventional deadlift","Set bar over midfoot, brace with neutral back, push floor away, stand tall, lower with hip hinge.","Get coaching before heavy loads."},
 {"Legs","Leg extension","Adjust machine knee pivot, straighten knees under control, lower slowly.","Use pain-free range, especially with knee discomfort."},
 {"Legs","Seated leg curl","Align knee joint with machine pivot, curl heels down and back, return slowly.","Do not jerk the pad."},
 {"Legs","Lying leg curl","Lie face-down, keep hips against pad, curl heels toward glutes, lower slowly.","Avoid arching back."},
 {"Legs","Walking lunge","Step forward, lower both knees, push through front foot, repeat alternating legs.","Use a short comfortable range if knees hurt."},
 {"Legs","Bulgarian split squat","Place rear foot on bench, lower straight down over front leg, stand through front foot.","Start bodyweight and hold support."},
 {"Legs","Hip thrust","Support upper back on bench, place feet flat, drive hips upward, squeeze glutes, lower.","Avoid overextending low back."},
 {"Legs","Standing calf raise","Stand on stable platform, rise onto toes, pause, lower heels slowly.","Use support for balance."},
 {"Core","Plank","Place forearms below shoulders, brace abs and glutes, keep body in straight line, breathe.","Stop when hips sag."},
 {"Core","Side plank","Support forearm below shoulder, lift hips, hold straight line from head to feet.","Modify by bending lower knee."},
 {"Core","Dead bug","Lie on back, brace abdomen, extend opposite arm and leg, return and alternate.","Keep low back gently supported."},
 {"Core","Bird dog","On hands and knees, extend opposite arm and leg, pause, return and switch.","Keep pelvis level."},
 {"Core","Cable crunch","Kneel holding rope near head, curl ribcage toward pelvis, return slowly.","Move spine gently, not just hips."},
 {"Core","Hanging knee raise","Hang from bar, brace core, lift knees toward chest, lower slowly.","Avoid swinging."},
 {"Cardio","Treadmill walking","Begin at easy pace, walk tall, increase speed or incline gradually.","Hold rails only as needed; use comfortable footwear."},
 {"Cardio","Stationary cycling","Adjust saddle so knee stays slightly bent at bottom, pedal smoothly, increase resistance gradually.","Keep knees tracking forward."},
 {"Cardio","Elliptical trainer","Stand upright on pedals, push and pull handles smoothly, maintain even stride.","Start low resistance."},
 {"Cardio","Rowing machine","Push with legs first, lean back slightly, pull handle to lower ribs; reverse arms, hips, legs.","Avoid rounding back."},
 {"Cardio","Jump rope","Keep jumps low, rotate rope with wrists, land softly on forefeet.","High impact; skip if knee pain."},
 {"Mobility","Cat-cow","On hands and knees, slowly round spine then extend gently with breathing.","Stay within pain-free range."},
 {"Mobility","Hip flexor stretch","Half-kneel, tuck pelvis slightly, shift forward until front hip stretches.","Avoid arching lower back."},
 {"Mobility","Hamstring stretch","Extend one leg, hinge gently at hips until stretch behind thigh.","Do not bounce."},
 {"Mobility","Thoracic rotation","On all fours, place hand behind head, rotate elbow upward, return.","Rotate upper back rather than forcing low back."}
 };
 void gym(){page("Gym exercise library");line("54 strength, cardio and mobility exercises. Tap any movement for technique and safety notes.");String last="";for(int i=0;i<EXERCISES.length;i++){final int index=i;if(!EXERCISES[i][0].equals(last)){last=EXERCISES[i][0];line("━━ "+last.toUpperCase()+" ━━");}button(EXERCISES[i][1],()->exerciseDetail(index));}button("← Dashboard",()->home());}
 void exerciseDetail(int i){String[] x=EXERCISES[i];page(x[1]);line("Muscle group: "+x[0]);line("HOW TO DO IT");line(x[2]);line("FORM & SAFETY");line(x[3]);line("Suggested starting point: 2–3 sets of 8–12 controlled reps for strength, or 10–20 minutes easy for cardio. Adjust for your ability and any injuries.");button("← All gym exercises",()->gym());}
 @Override public void onBackPressed(){home();}
}