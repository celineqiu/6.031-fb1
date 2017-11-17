// Testing strategy for GUI
// 
// Use visual tests to evaluate the correctness of the GUI/main program
//
// Flingball
//      no file specified -> default file
//      valid flingball file path -> specified file
//      valid flingball file path -> default file
//
// Game / Simulator
//      balls: 
//          0, 1, >1
//          initial velocity 0, >0
//      gadgets: 
//          Absorber, CircleBumper, SquareBumper, TriangleBumper
//          different Absorber sizes
//          different TriangleBumper orientations - 0, 90, 180, 270
//          Absorber triggered by another gadget
//          self-triggering Absorber
//          combinations of the different gadgets 
//      things to check for:
//          ball moves to bottom right corner of absorber
//          ball shoots straight up from absorber
//          ball collides with gadgets
//          gadgets change color with hit/triggered (excluding Absorber and Wall)
//          balls ejected with correct trigger
//          balls reflect off gadgets (except Absorber)
