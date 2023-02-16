//HangmanProject.java by Alex Dubljevic 01/27/2022 - Program which simulates "hangman" game where user guesses a word letter by letter with limited wrong answers. The program tells the user the amount of remaining letters, which letters they have already guessed, prevents duplicate input, has hints, has multiple word sets you can choose from, and it is replayable.

//Import statements
import java.util.*;
import java.io.*;
import java.util.concurrent.ThreadLocalRandom;

class Main {
  public static void main(String[] args) throws IOException{

    //Welcome the user and begin the game
    System.out.println("Welcome to online hangman!\nIn hangman, you will get a random word which you will have to guess.\nYou can guess this word letter by letter and it will reveal itself for each correct letter.\nBe careful though, you can only get a few wrong answers before you lose the game so choose wisely.");

    //Initialization of string to hold user answer for if they want to play again
    String answer;

    //Dowhile loop for replaying the game
    do{
      
      //String which stores user category selection from the chooseCategory method
      String category = chooseCategory();
      
      //Initialize both word and hint files using the user selection string above
      File file = new File(category + ".txt");
      File hint = new File(category + "Hint.txt");

      //Scanners for both words and hints txt files
      Scanner wordInput = new Scanner(file);
      Scanner hintInput = new Scanner(hint);

      //Initialize arraylists which will store all words and hints
      ArrayList<String> words = new ArrayList<>();
      ArrayList<String> hints = new ArrayList<>();

      //Goes through text files and populates arraylists with words and hints
      while (wordInput.hasNext()){
        words.add(wordInput.nextLine());
      }
      while (hintInput.hasNext()){
        hints.add(hintInput.nextLine());
      }

      //Closes Files
      wordInput.close();
      hintInput.close();

      //Generates random number to choose a random word/hint
      int random = randomInt(words.size());

      //Selects random word/hint as the chosen word/hint for this game
      String chosenWord = words.get(random);
      String chosenHint = hints.get(random);

      //Splits the word by each character and puts it into a new arraylist which will be used for the user to guess
      ArrayList<String> lettersLeft = new ArrayList<>(Arrays.asList(chosenWord.split("")));

      //Make and fill an Arraylist with blank characters not guessed by the user which will be displayed
      ArrayList<String> notGuessed = new ArrayList<>();
      for (int i = 0; i < lettersLeft.size(); i++){
        notGuessed.add("_");
        //Checks whether or not the word has a space and adjusts the hidden array
        String spaceChecker = lettersLeft.get(i);
        if(spaceChecker.equals(" ")){
          notGuessed.set(i, " ");
        }
      }

      //Prints the hint out
      System.out.println("\nYour hint is: " + chosenHint);

      //Boolean for ending the game, integer for the number of lives the user has remaining, and an arraylist to store letters that have been already guessed 
      boolean end = true;
      int lives = 6;
      ArrayList<String> alreadyGuessed = new ArrayList<>();

      //While loop which ends the game when an end condition is met
      while(end){
        
        //Method which prints different stages of hangman depending on how many lives are left
        printHangman(lives);

        //Int which stores how many letters the user has left to guess and prints that aswell as the underscore array
        int numberLeft = Collections.frequency(notGuessed, "_");
        System.out.println("\nYou have " + numberLeft + " letters left to guess.\n");
        for (int i = 0; i < notGuessed.size(); i++){
          System.out.print(notGuessed.get(i));
        }

        //For loop to print all the letters the user has already guessed
        System.out.print("\n\n\nYou have already guessed:");
        for (int i = 0; i < alreadyGuessed.size(); i++){
          System.out.print(alreadyGuessed.get(i) + " ");
        }

        //String to hold the user's guess 
        String guess = "";

        //Dowhile loop and boolean which checks if a user has already guessed a letter and loops it until they guess a new letter
        boolean newGuess = true;

        //Scanner for user input
        Scanner keyboard = new Scanner(System.in);
        
        do{
          //Gets user input and stores it in a string
          System.out.println("\n\nPlease enter the letter you would like to guess:");
          guess = keyboard.nextLine();

          //Checks whether or not the input was already guessed and stops looping if it wasnt
          if(!alreadyGuessed.contains(guess)){
            newGuess = false;
          }
          else{
            System.out.println("You have already guessed this letter, please guess another letter");
          }
        } while(newGuess);

        //Adds the guess to the already guessed letters if the input is valid
        alreadyGuessed.add(guess);

        //Adjusts the underscore array to have any letters the user has properly guessed
        for (int i = 0; i < lettersLeft.size(); i++){
          if(guess.equals(lettersLeft.get(i))){
            notGuessed.set(i, lettersLeft.get(i));
          }
        }

        //Subtracts one life if the word did not contain the guessed letter
        if(!chosenWord.contains(guess)){
          System.out.println("Oh no! Your word did not contain: " + guess);
          lives--;
        }

        //Checks if the user has run out of lives and end game if they have
        if(lives == 0){
          end = false;
        }

        //Checks if there are no letters left to guess, and ends the game if there are none because the user would've won
        if(0 == Collections.frequency(notGuessed, "_")){
          end = false;
        }
      } //End of loop which ends the game

      //End game scenario, congratulates user if they won and says better luck next time if they lose
      if(lives == 0){
      System.out.println("_____\n|/   |\n|   (_)\n|   \\|/\n|    |\n|   / \\\n|\n|_____");
      System.out.println("You lost! Better luck next time! Your word was: " + chosenWord);
      }
      else{
        System.out.println("\nYour word was: " + chosenWord + "\nCongratulations, you got the entire word! You have won!!");
      }

      //Run the playAgain method to determine whether or not to loop the program
      answer = playAgain();
    
    //Loop the program again if the user chooses to play again
    } while (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y"));

    //Shutdown message
    System.out.println("Program successfully shutdown.");
  }//End of main method

  /**
  Method which asks the user what category they would like to choose, stores that input, then returns their input as a string
  @return the user input string converted from 1, 2 or 3 into noun, adj or verb
  */

  public static String chooseCategory(){
    //Asking the user what category they want to choose
    System.out.println("Please select a category of words: \nNouns (1)\nAdjectives (2) \nVerbs (3)");
    
    //Scanner for user input
    Scanner keyboard = new Scanner(System.in);

    //Gets user input and stores it in a string
    String selection = keyboard.nextLine();

    //If and elseif statements for the various options of wordsets, each statement re-initializes both files and replaces them with the files with words in them
    if (selection.equals("1")) {
      return "noun";
    }
    else if (selection.equals("2")) {
      return "adj";
    }
    else if (selection.equals("3")) {
      return "verb";
    } 
    else {
      System.out.println("You did not enter a valid input. The program will now shutdown.");
      System.exit(0);
      return "";
    }
  } //End of chooseCategory method

  /**
  Method which generates a random number which is within the range of the number of words available in the wordbank
  @param biggest is the greatest int the method will generate
  @return the generated random int from 0 to the biggest number
  */

  public static int randomInt(int biggest){
    return ThreadLocalRandom.current().nextInt(0, biggest);
  } //End of randomInt method

  /**
  Method which asks the user if they want to play again and returns their input as a string
  @return String which contains the user's response
  */

  public static String playAgain(){
    
    //Scanner for user input
    Scanner keyboard = new Scanner(System.in);

    //Ask the user if they want to play again
    System.out.println("\nWould you like to play again? (yes/no)");

    //Get user input and return it
    return keyboard.nextLine();
  } //End of playAgain method

  /**
  Method which takes in the number of lives remaining as an int, then goes through and finds the corresponding hangman drawing for the number of lives left and prints it to the console.
  @param lives is an int which is the number of lives remaining
  */

  public static void printHangman(int lives){

    //If statements for printing out the hangman art for remaining lives 6-1
    if(lives ==6){
      System.out.println("_____\n|/   |\n|   \n|    \n|    \n|    \n|\n|_____");
    }

    if(lives == 5){
      System.out.println("_____\n|/   |\n|   (_)\n|    \n|    \n|    \n|\n|_____");
    }

    if(lives == 4){
      System.out.println("_____\n|/   |\n|   (_)\n|    |\n|    |\n|    \n|\n|_____");
    }

    if(lives == 3){
      System.out.println("_____\n|/   |\n|   (_)\n|   \\|\n|    |\n|    \n|\n|_____");
    }

    if(lives == 2){
      System.out.println("_____\n|/   |\n|   (_)\n|   \\|/\n|    |\n|    \n|\n|_____");
    }

    if(lives == 1){
      System.out.println("_____\n|/   |\n|   (_)\n|   \\|/\n|    |\n|   /\n|\n|_____");
    }
  } //End of printHangman method
} //End of main class