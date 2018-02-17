# Langton-s-Ant
Langton's Ant Simulation : An Android Application

This is my take on Langton's Ant. From Wikipedia (https://en.wikipedia.org/wiki/Langton%27s_ant):

" Langton's ant is a two-dimensional universal Turing machine with a very simple set of rules but complex emergent behavior. It was invented by Chris Langton in 1986 and runs on a square lattice of black and white cells. -- 
Squares on a plane are colored variously either black or white. We arbitrarily identify one square as the "ant". The ant can travel in any of the four cardinal directions at each step it takes. The "ant" moves according to the rules below:

    At a white square, turn 90° right, flip the color of the square, move forward one unit
    At a black square, turn 90° left, flip the color of the square, move forward one unit
"

In my application you can set the initial conditions of the grid cells by tapping them. You can also set the starting position of the ant in similar manner. If the ant tries to step out of the bounds of the square lattice it emerges at the opposite end of it. I have tested the application on my OnePlus 3 as well as an emulated Nexus S.
