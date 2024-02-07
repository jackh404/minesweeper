import java.util.Scanner;
public class MineSweeper
{
    //2D Arrays for mine placement and covered / uncovered status
    private int[][] board;
    private boolean[][] flipped;
    private boolean[][] marked;
    public boolean gameOver = false;
    private int numMines = 0;
    private int numFlipped = 0;

    public static Scanner scan = new Scanner(System.in);

//Constructor
    public MineSweeper(int rows, int cols)
    {
        board = new int[rows][cols];
        flipped = new boolean[rows][cols];
        marked = new boolean[rows][cols];
        fillMines();
    }

    public void fillMines()
    {
        for(int row = 0; row < board.length; row++)
        {
            for(int tile = 0; tile < board[0].length; tile++)
            {
                int bit = (int) (Math.random()*9);
                if(bit == 1)
                {
                    board[row][tile] = -1;
                    numMines++;
                }
            }
        }
    }
    public void fillTiles()
    {
        for(int row = 0; row < board.length; row++)
        {
            for(int tile = 0; tile < board[0].length; tile++)
            {
                if(board[row][tile] < 0)
                {
                    for(int subRow = row-1; subRow <= row +1; subRow++)
                    {
                        for(int subTile = tile-1; subTile <= tile+1; subTile++)
                        {
                            if(subRow >= 0 && subRow < board.length && subTile >= 0 && subTile < board[0].length && board[subRow][subTile] >= 0)
                            {
                                board[subRow][subTile]++;
                            }
                        }
                    }
                }
            }
        }
    }
    public void printBoard()
    {
        System.out.println();
        for(int r = -1; r < board.length; r++)
        {
            for(int t = -1; t < board[0].length; t++)
            {
                if(r == -1)
                {
                    System.out.print(t+1+" ");
                }
                else if(t == -1)
                {
                    System.out.print(r+1+" ");
                }
                else if(flipped[r][t] == true)
                {
                    if(board[r][t]==0)
                    {
                        System.out.print("  ");
                    }
                    else 
                    {
                        System.out.print(board[r][t]+" ");
                    }
                }
                else if(marked[r][t] == true)
                {
                    System.out.print("M ");
                }
                else
                {
                    System.out.print("X ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    public void unCover(int r, int c)
    {
        
        //if flipped tile is a mine, game over
        if(board[r][c]==-1)
        {
            if(marked[r][c] == false)
            {
                gameOver = true;
                flipped[r][c]=true;
                System.out.println("Game Over!!");
            }
            else
            {
                System.out.println("What are you crazy?? You marked that tile as a mine!!");
            }
        }
        //if flipped tile has no adjacent mines, check surrounding tiles and flip as appropriate
        else if(board[r][c] == 0)
        {
            flipped[r][c]=true;
            numFlipped++;
            for(int subRow = r-1; subRow <= r+1; subRow++)
            {
                for(int subCol = c-1; subCol <= c+1; subCol++)
                {
                    if(subRow >= 0 && subRow < board.length && subCol >=0 && subCol < board[0].length //checking to make sure adjacent cell is within bounds
                    && board[subRow][subCol] > -1 && flipped[subRow][subCol] == false) //making sure adjacent cell is not a mine & not flipped
                    {
                        unCover(subRow,subCol); //recursively call the unCover method on appropriate tiles adjacent to "0" tiles
                    }
                }
            }
        }
        //Tile gets flipped no matter what (unless it's a marked mine)
        else {
            flipped[r][c]=true;
            numFlipped++;
        }
        printBoard();
        if(numFlipped == (board.length*board[0].length)-numMines)
        {
            checkWin();
        }
    }
    public void pickTile()
    {
        System.out.println("Pick a tile by typing the row number and column number separated by a space. To mark or unmark a tile as a mine, type \"M\" afterward. Then press enter.");
        String tile = scan.nextLine();
        tile = tile.toUpperCase();
        String row = tile.substring(0,tile.indexOf(" "));
        String col = tile.substring(tile.indexOf(" ")+1,tile.indexOf(" ")+2);
        int r = Integer.parseInt(row)-1;
        int c = Integer.parseInt(col)-1;
        if(tile.indexOf("M")>=0)
        {
            if(marked[r][c] == false)
            {
                marked[r][c] = true;
                printBoard();
                if(numFlipped == (board.length*board[0].length)-numMines)
                {
                    checkWin();
                }
            }
            else {
                marked[r][c] = false;
                printBoard();
            }
        }
        else 
        {
            unCover(r,c);
        }
    }

    //Checks for win condition and displays text when it is met.
    public void checkWin()
    {
        boolean won = true;
        for(int r = 0; r < board.length; r++)
        {
            for(int c =0; c < board[r].length; c++)
            {
                if(board[r][c]==-1 && !marked[r][c])
                {
                    won = false;
                }
            }
        }
        if(won)
        {
            gameOver = true;
            System.out.println("You did it! You marked all the mines! Good luck on the walk home! :)");
        }
    }

    public static void main(String[] args)
    {
        System.out.println("Welcome to Minesweeper!");
        MineSweeper mine = new MineSweeper(6,6);
        mine.fillMines();
        mine.fillTiles();
        mine.printBoard();
        while(!mine.gameOver)
        {
            mine.pickTile();
        }

    }

}
