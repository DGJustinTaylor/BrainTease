package com.taylorj.braintease;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
{
    private Button btnGenerateF;
    private Button btnGenerateJ;
    private TextView txtMainFact;
    private String last;
    public static DatabaseManager dbManager;

    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;

    SharedPreferences mPrefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize variables
        dbManager = new DatabaseManager(this);
        btnGenerateF = (Button) findViewById(R.id.btnGenerateF);
        btnGenerateJ = (Button) findViewById(R.id.btnGenerateJ);
        txtMainFact = (TextView) findViewById(R.id.txtMainFact);
        dl = (DrawerLayout) findViewById(R.id.dlMenuSide);
        abdt = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(abdt);
        abdt.syncState();

        mPrefs = getSharedPreferences("myPref", 0);

        Boolean firstRun = mPrefs.getBoolean("FirstLoad", true);

        if(firstRun)
        {
            initDB();
            mPrefs.edit().putBoolean("FirstLoad", false).commit();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Setup the navigation menu
        NavigationView navView = (NavigationView) findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                int id = menuItem.getItemId();

                if(id == R.id.menFactList)
                {
                    openFactList();
                }
                else if(id == R.id.menJokeList)
                {
                    openJokeList();
                }
                else if(id == R.id.menPreferences)
                {
                    openPreferences();
                }

                return true;
            }
        });

        //Button that loads a new fact/joke while the user is in the app
        btnGenerateF.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                    loadFact();
            }
        });

        btnGenerateJ.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loadJoke();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        last = NotificationReceiver.theMessage;

        if(last != null && last != "")
        {
            txtMainFact.setText(last);
        }

        else
        {
            txtMainFact.setText("Generate a fact or a joke!");
        }
    }

    //Loads the menu and menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    //Opens Activity2 (Fact List)
    public void openFactList()
    {
        Intent intent = new Intent(this, FactList.class);
        startActivity(intent);
    }

    public void openJokeList()
    {
        Intent intent = new Intent(this, JokeList.class);
        startActivity(intent);
    }

    public void openPreferences()
    {
        Intent intent = new Intent(this, Preferences.class);
        startActivity(intent);
    }

    //Pulls a fact from the database
    private void loadFact()
    {
        String fact = dbManager.singleFact();

        txtMainFact.setText(fact);
    }

    private void loadJoke()
    {
        String joke = dbManager.singleJoke();

        txtMainFact.setText(joke);
    }

    private void initDB()
    {
        dbManager.insertNewFact("Banging your head against a wall for one hour burns 150 calories.");
        dbManager.insertNewFact("In Switzerland it is illegal to own just one guinea pig.");
        dbManager.insertNewFact("Pteronophobia is the fear of being tickled by feathers.");
        dbManager.insertNewFact("Snakes can help predict earthquakes.");
        dbManager.insertNewFact("A flock of crows is known as a murder.");
        dbManager.insertNewFact("The oldest 'your mom' joke was discovered on a 3,500 year old Babylonian tablet.");
        dbManager.insertNewFact("So far, two diseases have been successfully eradicted: smallpox and rinderpest.");
        dbManager.insertNewFact("May 29th is officially 'Put a Pillow on Your Fridge Day'.");
        dbManager.insertNewFact("Cherophobia is an irrational fear of fun or happiness.");
        dbManager.insertNewFact("7% of American adults believes that chocolate milk comes from brown cows.");
        dbManager.insertNewFact("If you lift a kangaroo's tail off the ground it can't hop.");
        dbManager.insertNewFact("Bananas are curved because they grow towards the sun.");
        dbManager.insertNewFact("Billy goats urinate on their own heads to smell more attractive to females.");
        dbManager.insertNewFact("The inventor of the Frisbee was cremated and made into a Frisbee after he died.");
        dbManager.insertNewFact("During your lifetime, you will produce enough saliva to fill two swimming pools.");
        dbManager.insertNewFact("If Pinocchio says 'My nose will grow now,' it would cause a paradox.");
        dbManager.insertNewFact("Polar bears could eat as many as 86 penguins in a single sitting...");
        dbManager.insertNewFact("King Henry VIII slept with a gigantic axe beside him.");
        dbManager.insertNewFact("Movie trailers were originally shown after the movie, which is why they are called 'trailers'.");
        dbManager.insertNewFact("An eagle can kill a young deer and fly away with it.");
        dbManager.insertNewFact("Heart attacks are more likely to happen on a Monday.");
        dbManager.insertNewFact("Tennis players are not allowed to swear when they are playing in Wimbledon.");
        dbManager.insertNewFact("In 2017 more people were killed from injuries caused by taking a selfie than from shark attacks.");
        dbManager.insertNewFact("The top six foods that make you fart are beans, corn, bell peppers, cauliflower, cabbage and milk.");
        dbManager.insertNewFact("There is a species of spider called the Hobo Spider.");
        dbManager.insertNewFact("A lion's roar can be heard from 5 miles away.");
        dbManager.insertNewFact("Saint Lucia is the only country in the world named after a woman.");
        dbManager.insertNewFact("A baby spider is called a spiderling.");
        dbManager.insertNewFact("The United States Navy has started using Xbox controllers for their periscopes.");
        dbManager.insertNewFact("The following can be read forward and backwards: Do geese see God?");
        dbManager.insertNewFact("A baby octopus is about the size of a flea when it is born.");
        dbManager.insertNewFact("A sheep, a duck and a rooster were the first passengers in a hot air balloon.");
        dbManager.insertNewFact("In Uganda, around 48% of the population is under 15 years of age.");
        dbManager.insertNewFact("The average male gets bored of a shopping trip after 26 minutes.");
        dbManager.insertNewFact("In the 16th century, Arab women could initiate a divorce if their husbands didn't pour coffee for them.");
        dbManager.insertNewFact("Recycling one glass jar saves enough energy to watch television for 3 hours.");
        dbManager.insertNewFact("After the premiere of '16 and Pregnant,' teen pregnancy rates dropped.");
        dbManager.insertNewFact("Approximately 10-20% of U.S. power outages are caused by squirrels.");
        dbManager.insertNewFact("Facebook, Instagram and Twitter are all banned in China.");
        dbManager.insertNewFact("95% of people text things they could never say in person.");
        dbManager.insertNewFact("Honeybeens can recognize human faces.");
        dbManager.insertNewFact("The Battle of Hastings didn't take place in Hastings.");
        dbManager.insertNewFact("While trying to find a cure for AIDS, the Mayo Clinic made glow in the dark cats.");
        dbManager.insertNewFact("A swarm of 20,000 bees followed a car for two days because their queen was stuck inside.");
        dbManager.insertNewFact("Nearly 3% of the ice in Antarctic glaciers is penguin urine.");
        dbManager.insertNewFact("Bob Dylan's real name is Robert Zimmerman.");
        dbManager.insertNewFact("A crocodile can't poke its tongue out.");
        dbManager.insertNewFact("Sea otters hold hands when they sleep so they don't drift away from each other.");
        dbManager.insertNewFact("A small child could swim through the veins of a blue whale.");
        dbManager.insertNewFact("Bin Laden's death was announced on May 1st 2011. Hitler's death was announce May 1st 1945.");
        dbManager.insertNewFact("J.K. Rowling chose the unusual name 'Hermoine' so young girls wouldn't be teased for being nerdy.");
        dbManager.insertNewFact("Hewlett-Packard's name was decided in a coin toss in 1939.");
        dbManager.insertNewFact("There is a total of 1,665 steps in the Eiffel Tower.");
        dbManager.insertNewFact("The Pokemon Hitmonlee and Hitmonchan are based off of Bruce Lee and Jackie Chan respectively.");
        dbManager.insertNewFact("A woman once tried to commit suicide by jumping off the Empire State Building. She jumped from the 86th floor but was blown back onto the 85th floor by a gust of wind.");
        dbManager.insertNewFact("Pirates wore earrings because they believed it improved their eyesight.");
        dbManager.insertNewFact("Los Angeles's full name is 'El Pueblo de Nuestra Senora la Reina de los Angeles de Porciuncula.'");
        dbManager.insertNewFact("The Twitter bird actually has a name - Larry.");
        dbManager.insertNewFact("Octopuses have four pairs of arms.");
        dbManager.insertNewFact("In the popular sitcom, Parks and Recreation, the writers had no idea Nick Offerman was a talented saxophone player when they wrote the Duke Silver plot line.");
        dbManager.insertNewFact("It snowed in the sahara desert for 30 minutes on the 18th of February, 1979.");
        dbManager.insertNewFact("Mike Tyson once offered a zoo attendant 10,000 dollars to let him fight a gorilla.");
        dbManager.insertNewFact("ABBA turned down 1 billion dollars to do a reunion tour.");
        dbManager.insertNewFact("There has never been a verified snow leopard attack on a human being.");
        dbManager.insertNewFact("The first ever alarm clock could only ring at 4 a.m.");
        dbManager.insertNewFact("Birds don't urinate.");
        dbManager.insertNewFact("Dying is illegal in the Houses of Parliaments.");
        dbManager.insertNewFact("The most venomous jellyfish in the world is the Irukandji.");
        dbManager.insertNewFact("The 20th of March is Snowman Burning Day.");
        dbManager.insertNewFact("Queen Elizabeth can't sit on the Iron Throne from Game of Thrones because it counts as a 'foreign throne.'");
        dbManager.insertNewFact("There is an official Wizard of New Zealand.");
        dbManager.insertNewFact("An apple, potato, and onion all taste the same if you eat them with your nose plugged.");
        dbManager.insertNewFact("Vincent van Gogh only sold one painting in his lifetime.");
        dbManager.insertNewFact("A company in Taiwan makes dinnerware out of wheat, so you can eat your plate.");
        dbManager.insertNewFact("The average person walks the equivalent of five times around the world in their lifetime.");
        dbManager.insertNewFact("Michael Jackson offered to make a Harry Potter musical, but J.K. Rowling rejected the idea.");
        dbManager.insertNewFact("The world record for stuffing drinking straws into your mouth at once is 459.");
        dbManager.insertNewFact("Nutella was invented during WWII, when hazelnuts were mixed intro chocolate to extend chocolate rations.");
        dbManager.insertNewFact("In 2011, more than 1 in 3 divorce filings in the U.S. contained the word 'Facebook.'");
        dbManager.insertNewFact("According to Genesis 1:20-22 the chicken came before the egg.");
        dbManager.insertNewFact("Honeybees can get drunk on fermented tree sap.");
        dbManager.insertNewFact("Tears contain a natural pain killer which reduces pain and improves your mood.");
        dbManager.insertNewFact("Squirrels forget where they hide about half of their nuts.");
        dbManager.insertNewFact("Millions of birds a year die from smashing into windows in the U.S. alone.");
        dbManager.insertNewFact("Dolly Parton lost in a Dolly Parton look-alike contest.");
        dbManager.insertNewFact("George W. Bush was once a cheerleader.");
        dbManager.insertNewFact("In total, there are 205 bones in the skeleton of a horse.");
        dbManager.insertNewFact("Coca-Cola owns all website URLs that can be read as 'ahh,' all the way up to 62 h's.");
        dbManager.insertNewFact("Each year there are more than 40,000 toilet related injuries in the United States.");
        dbManager.insertNewFact("Strawberries can be red, yellow, green or white.");
        dbManager.insertNewFact("MewTwo is a clone of the Pokemon Mew, yet it comes before Mew in the Pokedex.");
        dbManager.insertNewFact("Four people lived in a home for 6 months infested with about 2,000 brown recluse spiders, but none of them were harmed.");
        dbManager.insertNewFact("Madonna suffers from brontophobia, which is the fear of thunder.");
        dbManager.insertNewFact("In June 2017, the Facebook community reached 2 billion active users.");
        dbManager.insertNewFact("Samual L. Jackson requested to have a purple lightsaber in Star Wars in order for him to accept the roll as Mace Windu.");
        dbManager.insertNewFact("Paraskavedekatriaphobia is the fear of Friday the 13th.");
        dbManager.insertNewFact("Kleenex tissues were originally used as filters in gas masks.");
        dbManager.insertNewFact("In 1998, Sony accidentally sold 700,000 camcorders that had the technology to see through peoples clothes.");
        dbManager.insertNewFact("During your lifetime, you will spend around thirty-eight days brushing your teeth.");
        dbManager.insertNewFact("Ronald McDonald is 'Donald McDonald' in Japan because it makes pronunciation easier for the Japanese.");
        dbManager.insertNewFact("The welsh slang for 'Jellyfish' is Psygod Wibli Wobli. This literally translates to wibbly wobbly fish. This is in both lists because it is also funny.");

        dbManager.insertNewJoke("Did you hear about the monkeys who shared an Amazon account? They were Prime mates.");
        dbManager.insertNewJoke("I thought I won the argument with my wife as to how to arrange the dining room furniture, but when I got home the tables were turned.");
        dbManager.insertNewJoke("I once fell in love with a girl who only knew 4 vowels. She didn't know I existed.");
        dbManager.insertNewJoke("Not to brag, but I already have a date for Valentines Day. February 14th.");
        dbManager.insertNewJoke("My wife says that I wasted money by ordering a 3-meter wide frame for our wedding photo. I think she should look at the bigger picture.");
        dbManager.insertNewJoke("Why should you stand in the corner if it's cold? It's always 90 degrees.");
        dbManager.insertNewJoke("Got a frantic call from a woman who claimed she had overdosed and needed help immediately. We arrive on scene, and she hands us an empty mint container, saying she took them all. That night she learned that you cannot overdose on mints.");
        dbManager.insertNewJoke("Yesterday a clown held the door open for me. It was such a nice jester!");
        dbManager.insertNewJoke("The machine at the coin factory just suddenly stopped working, with no explanation. It doesn't make any cents!");
        dbManager.insertNewJoke("I was going to make myself a belt made out of watches, but I realized it would be a waist of time.");
        dbManager.insertNewJoke("Why can't you run through a campground? You can only ran, because it's past tents.");
        dbManager.insertNewJoke("I'm only friends with 25 letters of the alphabet. I don't know Y.");
        dbManager.insertNewJoke("Shout out to the people who ask what the opposite of 'in' is.");
        dbManager.insertNewJoke("I asked my French friend if she likes to play video games. She said, 'Wii.'");
        dbManager.insertNewJoke("Which country's capital has the fastest-growing population? Ireland, Everyday it's Dublin.");
        dbManager.insertNewJoke("Why was King Arthur's army too tired to fight? It had too many sleepless knights.");
        dbManager.insertNewJoke("I'm a big fan of whiteboards. I find them quite re-markable.");
        dbManager.insertNewJoke("Don't interrupt someone working on a puzzle. Chances are, you'll hear some crosswords.");
        dbManager.insertNewJoke("How much money does a pirate pay for corn? A buccaneer.");
        dbManager.insertNewJoke("6:30 is the best time on a clock. Hands down.");
        dbManager.insertNewJoke("I had to clean my spice rack and found everything was too old and had to be thrown out. What a waste of thyme.");
        dbManager.insertNewJoke("I lost me job at the bank on my very first day. A woman asked me to check her balance, so I pushed her over.");
        dbManager.insertNewJoke("I hate how funerals are always at 9a.m. I'm not really a mourning person.");
        dbManager.insertNewJoke("Did you hear about the man who was accidentally buried alive? It was a grave mistake.");
        dbManager.insertNewJoke("My ex used to hit me with stringed instruments. If only I had known about her history of violins.");
        dbManager.insertNewJoke("My friend tried to annoy me with bird puns, but I soon realized that toucan play at that game.");
        dbManager.insertNewJoke("Someone stole my toilet and the police have nothing to go on.");
        dbManager.insertNewJoke("Did you hear about the guy who got hit in the head with a can of soda? He was lucky it was a soft drink.");
        dbManager.insertNewJoke("I can't believe I got fired from the calendar factory. All I did was take a day off.");
        dbManager.insertNewJoke("Becoming a vegetarian is a huge missed steak.");
        dbManager.insertNewJoke("Why did the soda crusher quit his job? Because it was soda pressing.");
        dbManager.insertNewJoke("Why is Peter Pan always flying? He neverlands.");
        dbManager.insertNewJoke("Did you hear about the mathematician who was afraid of negative numbers? He'd stop at nothing to avoid them.");
        dbManager.insertNewJoke("Police were called to a daycare center where a three-year-old was resisting a rest.");
        dbManager.insertNewJoke("What did the librarian say when the books were a mess? We should be ashamed of ourshelves.");
        dbManager.insertNewJoke("Acupuncture is a jab well done.");
        dbManager.insertNewJoke("Time flies like an arrow. Fruit flies like a banana.");
        dbManager.insertNewJoke("Why did the tomato blush? Because it saw the salad dressing.");
        dbManager.insertNewJoke("When she saw her first strands of grey, she thought she'd dye.");
        dbManager.insertNewJoke("I wrote a song about a tortilla. Well actually, it's more of a wrap");
        dbManager.insertNewJoke("Why do hamburgers go to the gym? To get better buns.");
        dbManager.insertNewJoke("What did the beach say as the tide came in? Long time, no sea.");
        dbManager.insertNewJoke("Did you hear about the boy who tried to catch fog? He mist.");
        dbManager.insertNewJoke("I went to a seafood disco last week. I pulled a mussel.");
        dbManager.insertNewJoke("Why did the apricot ask a prune to dinner? Because he couldn't find a date.");
        dbManager.insertNewJoke("So what if I can't spell Armageddon? It's not the end of the world.");
        dbManager.insertNewJoke("What do you get from a pampered cow? Spoiled milk.");
        dbManager.insertNewJoke("I wanted to learn how to drive a stick shift, but I couldn't find a manual.");
        dbManager.insertNewJoke("Why did the gym close down? It just didn't work out.");
        dbManager.insertNewJoke("How much room should you give fungi to grow? As much room as possible.");
        dbManager.insertNewJoke("How do trees get online? They just log in.");
        dbManager.insertNewJoke("The welsh slang for 'Jellyfish' is Psygod Wibli Wobli. This literally translates to wibbly wobbly fish. This is in both lists because it is also a fact.");
    }
}
