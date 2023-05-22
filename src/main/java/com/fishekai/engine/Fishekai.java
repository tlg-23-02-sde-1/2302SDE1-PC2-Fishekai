package com.fishekai.engine;

import com.fishekai.objects.Item;
import com.fishekai.objects.Location;
import com.fishekai.objects.Player;
import com.fishekai.utilities.Prompter;
import com.fishekai.utilities.SplashApp;

import java.util.HashMap;
import java.util.Scanner;

import static com.fishekai.utilities.Console.clear;
import static com.fishekai.utilities.Console.pause;

public class Fishekai implements SplashApp {

    // fields
    private boolean isGameOver = false;
    private HashMap<String, Location> locations; // will contain the locations loaded from JSON file
    private HashMap<String, Item> listOfItems = new HashMap<>();
    // instances
    private final Introduction intro = new Introduction();
    private final Prompter prompter = new Prompter(new Scanner(System.in));

    Player player = new Player("Ethan Rutherford", "Known for expertise in ancient artifacts.");
    Item amulet = new Item("amulet", "relic", "The thing that transported you to this place");


    Item parachute = new Item ("parachute", "item","Ripped and torn but some of the cords are still connected to the canopy");
    Item banana = new Item("banana", "food", "It's bananas, B.A.N.A.N.A.S");



    // methods
    public void start() {
        // show title here
        Display.showTitle();

        // ask user for input and store it
        String input = prompter.prompt("Would you like to play a new game? [Y]es or [N]o.\n><(((º> ",
                "Yes|yes|Y|y|No|no|N|n",
                "That is not a valid input\n");

        // if New Game, go to begin()
        if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y")) {
            begin();
        }

        // if Quit, terminates the game
        else if (input.equalsIgnoreCase("no") || input.equalsIgnoreCase("n")) {
            prompter.prompt("Are you sure?\n><(((º> ",
                    "Yes|yes|Y|y",
                    "This is not a valid option!\n");
        }
    }

    private void begin() {
        // show the intro
        intro.showIntro();

        // initialize data, crashes the jar build
        loadData();

        // set starting point
        Location current_location = locations.get("Beach");

        // starts the game
        while (!isGameOver) {
            // clear screen
            clear();

            // show display
            Display.showStatus(player, current_location);
            Display.showItem(current_location);

            // ask user for input
            String input = prompter.prompt("What would you like to do?\n><(((º> ");

            // give the input to the parser and then save the output of the parser
            //String[] words = UserInputParser.scan(input);

            // process input
            String[] words = input.split(" ");
            if (words.length > 0) {
                String verb = words[0].toLowerCase();

                switch (verb) { // change this to if statements
                    case "go":
                        if (words.length > 1) {
                            current_location.setHasBeenHere(true);
                            String direction = words[1].toLowerCase();
                            Location next_location = locations.get(current_location.getDirections().get(direction));
                            current_location = next_location;
                            pause(1_500);
                        } else {
                            System.out.println("Please specify a direction");
                        }
                        break;

                    case "look": // need more testing
                        if (words.length > 1) {
                            String itemToLook = words[1].toLowerCase();

                            if (current_location.getItems().containsKey(itemToLook)) {
                                System.out.println("The " + current_location.getItems().get(itemToLook).getName() + " looks like " + current_location.getItems().get(itemToLook).getDescription());
                                pause(1_000);
                            }
                            else if (player.getInventory().containsKey(itemToLook)) {
                                System.out.println("I'm looking here");
//                                System.out.println("The " + player.getInventory().get(itemToLook).getName() + " looks like " + player.getInventory().get(itemToLook).getDescription());
                                pause(1_000);
                            }
                            else {
                                System.out.println("There is no " + itemToLook + " here.");
                            }
                        } else {
                            // Handle the case when the user didn't specify an item to look at
                            System.out.println("Please specify an item to look at.");
                        }
                        break;

                    case "help":
                        clear();
                        Display.showHelp();
                        intro.askToContinue();
                        break;

                    case "quit":
                        isGameOver = true;
                        gameOver();
                        break;

                    default:
                        System.out.println("I don't understand. Type help for a list of commands.");
                        pause(1_500);
                        break;
                }
            }
            else {
                System.out.println("I don't understand. Type help for a list of commands.");
                pause(1_500);
            }

        }
    }


    private void gameOver() {
        clear();
        System.out.println("Thank you for playing!");
        pause(2_000);
    }

    // load the data
    private void loadData() {
        locations = DataLoader.processLocations();
        listOfItems.put("parachute", parachute);
        listOfItems.put("banana", banana);
        locations.get("Jungle").setItems(listOfItems);

        player.getInventory().put("amulet", amulet);

//        List<Item> beach_item = new ArrayList<>();
//        beach.setItems(beach_item);
    }

}