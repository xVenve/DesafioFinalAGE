# Genetic and Evolutionary Algorithms Final Challenge

## GAME RULES

- This is an optimisation game in which you must program a space pod to drive through a series of checkpoints as fast as
  possible.
- Each checkpoint is located at the position indicated by x, y.
- The “checkpointIndex” indicates the order of the checkpoints given as initial input.
- The game takes place on a map 16,000 units wide and 9,000 units high. The coordinate X = 0, Y = 0 is the upper left
  pixel.
- The checkpoints work as follows:
    - The checkpoints are circular, with a radius of 600 units.
    - The layout of the checkpoints is set by the test cases (test_case files in JSON format).
    - There are no overlapping checkpoints
- The game ends after 600 turns or when all checkpoints have been passed during 3 turns (whichever comes first).
- The checkpoint is completed if the centre of the space pod falls within the 600-unit circle.
- To drive the space pod, the destination X and Y coordinates and a degree of acceleration must be specified.
- The car will drive to the destination with a maximum turning radius of 18 degrees and after the turn will apply
  acceleration.
- The game ends if an incorrect or impossible command is sent.
- If the player does not make a correct move in the correct time, he/she is immediately eliminated (1,000ms for the
  first move and 50ms for the turns).
- In each turn, the moves are calculated as follows:
    - **Acceleration**: the car turns towards its destination, by a maximum of 18°. The normalised heading vector of the
      car is multiplied by the thrust power (acceleration) divided by the mass. The result is added to the current
      velocity vector.
    - **Motion**: its velocity vector is added to the position of all objects to calculate their new positions.
    - **Friction**: the velocity vector is multiplied by a constant, then truncated. The constant is:
        - 0.85 for space pods.
    - Position values are truncated
    - The heading angle of the cars is expressed in degrees and rounded.
    - Angles are given in degrees and in relation to the X-axis (East = 0 degrees, South = 90 degrees).

## DATA FOR THE GAME - AT EACH TURN

- The program must first read the initialisation data from the standard input.
- First line: an integer number of control points, the number of all control points to be passed (all control points are
  repeated 3 times).
- Next lines of checkpoints: one line per checkpoint
- Each checkpoint is represented by 2 integers: checkpointX, checkpointY.
- Data provided by the game:
- The following lines are one per entity with this format (6 integers).
    - checkpointIndex, x, y, vx, vy, angle
        - checkpointIndex indicates the index of the next control point as indicated in the initial entries. x, y for
          the position of the entity.
        - vx, vy for the velocity vector of the entity.
        - angle. Heading angle in degrees between 0 and 360 for the car.
- Data input for the bot:
    - A line for your car: three integers X, Y and acceleration.
    - A text can be added at the end to be displayed on top of the car to be able to trace what strategy it is
      following (recommended).
- One line for driving the space pod
- Each input line is 3 integers, X, Y and thrust, optionally you can add an informative text string
- Constraints:
    - 9 <= checkpoints <= 24 Checkpoints.
    - 0 <= thrust <= 200 Acceleration
    - 0 <= angle <= 360 Angle
    - Response time for first turn ≤ 1000 ms
    - Response time per turn ≤ 50 ms

## RUN SERVER

1. Simulation
2. Run SkeletonMain.java
3. Go to [localhost:8888:test.html](HTTP://LOCALHOST:8888/TEST.HTML)