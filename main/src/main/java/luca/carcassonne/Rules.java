package luca.carcassonne;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class Rules {
    private Stack<Tile> availableTiles;

    Rules() {
        availableTiles = getStandardDeckOfTiles();
        assert availableTiles.size() == 72;
    }

    public Stack<Tile> getStandardDeckOfTiles() {
        Stack<Tile> tiles = new Stack<>();

        // 4x Monasteries
        for (int i = 0; i < 4; i++) {
            tiles.push(new Tile(SideFeature.FIELD, SideFeature.FIELD, SideFeature.FIELD, SideFeature.FIELD,
                    new HashSet<>() {
                        {
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.NNW);
                                    add(CardinalPoint.N);
                                    add(CardinalPoint.NNE);
                                    add(CardinalPoint.ENE);
                                    add(CardinalPoint.E);
                                    add(CardinalPoint.ESE);
                                    add(CardinalPoint.SSE);
                                    add(CardinalPoint.S);
                                    add(CardinalPoint.SSW);
                                    add(CardinalPoint.WSW);
                                    add(CardinalPoint.W);
                                    add(CardinalPoint.WNW);
                                }
                            }, new HashSet<>()));
                            add(new Monastery());
                        }
                    }, "Monastery"));
        }

        // 2x Monasteries with road
        for (int i = 0; i < 2; i++) {
            tiles.push(new Tile(SideFeature.FIELD, SideFeature.FIELD, SideFeature.ROAD, SideFeature.FIELD,
                    new HashSet<>() {
                        {
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.NNW);
                                    add(CardinalPoint.N);
                                    add(CardinalPoint.NNE);
                                    add(CardinalPoint.ENE);
                                    add(CardinalPoint.E);
                                    add(CardinalPoint.ESE);
                                    add(CardinalPoint.SSE);
                                    add(CardinalPoint.SSW);
                                    add(CardinalPoint.WSW);
                                    add(CardinalPoint.W);
                                    add(CardinalPoint.WNW);
                                }
                            }, new HashSet<>()));
                            add(new Monastery());
                            add(new Road(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.S);
                                }
                            }));
                        }
                    }, "Monastery with road"));
        }

        // 8x Straight road
        for (int i = 0; i < 8; i++) {
            tiles.push(new Tile(SideFeature.ROAD, SideFeature.FIELD, SideFeature.ROAD, SideFeature.FIELD,
                    new HashSet<>() {
                        {
                            HashSet<Castle> castles = new HashSet<>();
                            add(new Road(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.N);
                                    add(CardinalPoint.S);
                                }
                            }));
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.NNW);
                                    add(CardinalPoint.WNW);
                                    add(CardinalPoint.W);
                                    add(CardinalPoint.WSW);
                                    add(CardinalPoint.SSW);
                                }
                            }, castles));
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.NNE);
                                    add(CardinalPoint.ENE);
                                    add(CardinalPoint.E);
                                    add(CardinalPoint.ESE);
                                    add(CardinalPoint.SSE);
                                }
                            }, castles));

                        }
                    }, "Straight road"));
        }

        // 9x Curvy road
        for (int i = 0; i < 9; i++) {
            tiles.push(new Tile(SideFeature.ROAD, SideFeature.ROAD, SideFeature.FIELD, SideFeature.FIELD,
                    new HashSet<>() {
                        {
                            HashSet<Castle> castles = new HashSet<>();
                            add(new Road(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.N);
                                    add(CardinalPoint.E);
                                }
                            }));
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.NNW);
                                    add(CardinalPoint.WNW);
                                    add(CardinalPoint.W);
                                    add(CardinalPoint.WSW);
                                    add(CardinalPoint.SSW);
                                    add(CardinalPoint.S);
                                    add(CardinalPoint.SSE);
                                    add(CardinalPoint.ESE);
                                }
                            }, castles));
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.NNE);
                                    add(CardinalPoint.ENE);
                                }
                            }, castles));

                        }
                    }, "Curvy road"));
        }

        // 4x Three road intersection
        for (int i = 0; i < 4; i++) {
            tiles.push(new Tile(SideFeature.FIELD, SideFeature.ROAD, SideFeature.ROAD, SideFeature.ROAD,
                    new HashSet<>() {
                        {
                            HashSet<Castle> castles = new HashSet<>();
                            add(new Road(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.E);
                                }
                            }));
                            add(new Road(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.S);
                                }
                            }));
                            add(new Road(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.W);
                                }
                            }));
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.WNW);
                                    add(CardinalPoint.NNW);
                                    add(CardinalPoint.N);
                                    add(CardinalPoint.NNE);
                                    add(CardinalPoint.ENE);

                                }
                            }, castles));
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.WSW);
                                    add(CardinalPoint.SSW);
                                }
                            }, castles));
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.ESE);
                                    add(CardinalPoint.SSE);
                                }
                            }, castles));
                        }
                    }, "Three road intersection"));
        }

        // 1x Four road intersection
        for (int i = 0; i < 1; i++) {
            tiles.push(new Tile(SideFeature.ROAD, SideFeature.ROAD, SideFeature.ROAD, SideFeature.ROAD,
                    new HashSet<>() {
                        {
                            HashSet<Castle> castles = new HashSet<>();
                            add(new Road(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.N);
                                }
                            }));
                            add(new Road(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.E);
                                }
                            }));
                            add(new Road(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.S);
                                }
                            }));
                            add(new Road(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.W);
                                }
                            }));
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.WNW);
                                    add(CardinalPoint.NNW);
                                }
                            }, castles));
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.NNE);
                                    add(CardinalPoint.ENE);
                                }
                            }, castles));
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.ESE);
                                    add(CardinalPoint.SSE);
                                }
                            }, castles));
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.SSW);
                                    add(CardinalPoint.WSW);
                                }
                            }, castles));
                        }
                    }, "Four road intersection"));
        }

        // 5x Single castle
        for (int i = 0; i < 5; i++) {
            tiles.push(new Tile(SideFeature.CASTLE, SideFeature.FIELD, SideFeature.FIELD, SideFeature.FIELD,
                    new HashSet<>() {
                        {
                            HashSet<Castle> castles = new HashSet<>() {
                                {
                                    add(new Castle(new ArrayList<CardinalPoint>() {
                                        {
                                            add(CardinalPoint.NNE);
                                            add(CardinalPoint.N);
                                            add(CardinalPoint.NNW);
                                        }
                                    }, false));
                                }
                            };
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.ENE);
                                    add(CardinalPoint.E);
                                    add(CardinalPoint.ESE);
                                    add(CardinalPoint.SSE);
                                    add(CardinalPoint.S);
                                    add(CardinalPoint.SSW);
                                    add(CardinalPoint.WSW);
                                    add(CardinalPoint.W);
                                    add(CardinalPoint.WNW);
                                }
                            }, castles));
                            for (Castle castle : castles) {
                                add(castle);
                            }
                        }
                    }, "Single castle"));
        }

        // 4x Single castle with straight road
        for (int i = 0; i < 3; i++) {
            tiles.push(new Tile(SideFeature.CASTLE, SideFeature.ROAD, SideFeature.FIELD, SideFeature.ROAD,
                    new HashSet<Feature>() {
                        {
                            HashSet<Castle> castles = new HashSet<Castle>() {
                                {
                                    add(new Castle(new ArrayList<CardinalPoint>() {
                                        {
                                            add(CardinalPoint.NNE);
                                            add(CardinalPoint.N);
                                            add(CardinalPoint.NNW);
                                        }
                                    }, false));
                                }
                            };
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.ENE);
                                    add(CardinalPoint.WNW);
                                }
                            }, castles));
                            add(new Road(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.E);
                                    add(CardinalPoint.W);
                                }
                            }));
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.ESE);
                                    add(CardinalPoint.SSE);
                                    add(CardinalPoint.S);
                                    add(CardinalPoint.SSW);
                                    add(CardinalPoint.WSW);
                                }
                            }, new HashSet<Castle>()));
                            for (Castle castle : castles) {
                                add(castle);
                            }
                        }
                    }, "Single castle with straight road"));
        }

        // 3x Single castle with curvy road left
        for (int i = 0; i < 3; i++) {
            tiles.push(new Tile(SideFeature.CASTLE, SideFeature.FIELD, SideFeature.ROAD, SideFeature.ROAD,
                    new HashSet<Feature>() {
                        {
                            HashSet<Castle> castles = new HashSet<Castle>() {
                                {
                                    add(new Castle(new ArrayList<CardinalPoint>() {
                                        {
                                            add(CardinalPoint.NNE);
                                            add(CardinalPoint.N);
                                            add(CardinalPoint.NNW);
                                        }
                                    }, false));
                                }
                            };
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.WNW);
                                    add(CardinalPoint.ENE);
                                    add(CardinalPoint.E);
                                    add(CardinalPoint.ESE);
                                    add(CardinalPoint.SSE);
                                }
                            }, castles));
                            add(new Road(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.W);
                                    add(CardinalPoint.S);
                                }
                            }));
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.WSW);
                                    add(CardinalPoint.SSW);
                                }
                            }, new HashSet<Castle>()));
                            for (Castle castle : castles) {
                                add(castle);
                            }
                        }
                    }, "Single castle with curvy road left"));
        }

        // 3x Single castle with curvy road right
        for (int i = 0; i < 3; i++) {
            tiles.push(new Tile(SideFeature.CASTLE, SideFeature.ROAD, SideFeature.ROAD, SideFeature.FIELD,
                    new HashSet<Feature>() {
                        {
                            HashSet<Castle> castles = new HashSet<Castle>() {
                                {
                                    add(new Castle(new ArrayList<CardinalPoint>() {
                                        {
                                            add(CardinalPoint.NNE);
                                            add(CardinalPoint.N);
                                            add(CardinalPoint.NNW);
                                        }
                                    }, false));
                                }
                            };
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.ENE);
                                    add(CardinalPoint.SSW);
                                    add(CardinalPoint.WSW);
                                    add(CardinalPoint.W);
                                    add(CardinalPoint.WNW);
                                }
                            }, castles));
                            add(new Road(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.E);
                                    add(CardinalPoint.S);
                                }
                            }));
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.ESE);
                                    add(CardinalPoint.SSE);
                                }
                            }, new HashSet<Castle>()));
                            for (Castle castle : castles) {
                                add(castle);
                            }
                        }
                    }, "Single castle with curvy road right"));
        }

        // 3x Single castle with three road intersection
        for (int i = 0; i < 3; i++) {
            tiles.push(new Tile(SideFeature.CASTLE, SideFeature.ROAD, SideFeature.ROAD, SideFeature.ROAD,
                    new HashSet<>() {
                        {
                            HashSet<Castle> castles = new HashSet<>() {
                                {
                                    add(new Castle(new ArrayList<CardinalPoint>() {
                                        {
                                            add(CardinalPoint.NNE);
                                            add(CardinalPoint.N);
                                            add(CardinalPoint.NNW);
                                        }
                                    }, false));
                                }

                            };
                            add(new Road(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.E);
                                }
                            }));
                            add(new Road(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.S);
                                }
                            }));
                            add(new Road(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.W);
                                }
                            }));
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.WNW);
                                    add(CardinalPoint.NNW);
                                    add(CardinalPoint.N);
                                    add(CardinalPoint.NNE);
                                    add(CardinalPoint.ENE);

                                }
                            }, castles));
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.WSW);
                                    add(CardinalPoint.SSW);
                                }
                            }, castles));
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.ESE);
                                    add(CardinalPoint.SSE);
                                }
                            }, castles));
                            for (Castle castle : castles) {
                                add(castle);
                            }
                        }
                    }, "Single castle with three road intersection"));

        }

        // 1x Long castle
        for (int i = 0; i < 1; i++) {
            tiles.push(new Tile(SideFeature.FIELD, SideFeature.CASTLE, SideFeature.FIELD, SideFeature.CASTLE,
                    new HashSet<Feature>() {
                        {
                            HashSet<Castle> castles = new HashSet<Castle>() {
                                {
                                    add(new Castle(new ArrayList<CardinalPoint>() {
                                        {
                                            add(CardinalPoint.WNW);
                                            add(CardinalPoint.W);
                                            add(CardinalPoint.WSW);
                                            add(CardinalPoint.ENE);
                                            add(CardinalPoint.E);
                                            add(CardinalPoint.ESE);
                                        }
                                    }, false));
                                }
                            };
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.NNW);
                                    add(CardinalPoint.N);
                                    add(CardinalPoint.NNE);
                                }
                            }, castles));
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.SSW);
                                    add(CardinalPoint.S);
                                    add(CardinalPoint.SSE);
                                }
                            }, castles));
                            for (Castle castle : castles) {
                                add(castle);
                            }
                        }
                    }, "Long castle"));
        }

        // 3x Curvy castle
        for (int i = 0; i < 3; i++) {
            tiles.push(new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.FIELD, SideFeature.FIELD,
                    new HashSet<>() {
                        {
                            HashSet<Castle> castles = new HashSet<>() {
                                {
                                    add(new Castle(new ArrayList<CardinalPoint>() {
                                        {
                                            add(CardinalPoint.NNW);
                                            add(CardinalPoint.N);
                                            add(CardinalPoint.NNE);
                                            add(CardinalPoint.ENE);
                                            add(CardinalPoint.E);
                                            add(CardinalPoint.ESE);
                                        }
                                    }, false));
                                }
                            };
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.WNW);
                                    add(CardinalPoint.W);
                                    add(CardinalPoint.WSW);
                                    add(CardinalPoint.SSW);
                                    add(CardinalPoint.S);
                                    add(CardinalPoint.SSE);
                                }
                            }, castles));
                            add(castles.iterator().next());

                        }
                    }, "Curvy castle"));
        }

        // 3x Two single castles opposite
        for (int i = 0; i < 3; i++) {
            tiles.push(new Tile(SideFeature.CASTLE, SideFeature.FIELD, SideFeature.CASTLE, SideFeature.FIELD,
                    new HashSet<>() {
                        {
                            HashSet<Castle> castles = new HashSet<>() {
                                {
                                    add(new Castle(new ArrayList<CardinalPoint>() {
                                        {
                                            add(CardinalPoint.NNE);
                                            add(CardinalPoint.N);
                                            add(CardinalPoint.NNW);
                                        }
                                    }, false));
                                    add(new Castle(new ArrayList<CardinalPoint>() {
                                        {
                                            add(CardinalPoint.SSE);
                                            add(CardinalPoint.S);
                                            add(CardinalPoint.SSW);
                                        }
                                    }, false));
                                }
                            };
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.ENE);
                                    add(CardinalPoint.E);
                                    add(CardinalPoint.ESE);
                                    add(CardinalPoint.WSW);
                                    add(CardinalPoint.W);
                                    add(CardinalPoint.WNW);
                                }
                            }, castles));
                            for (Castle castle : castles) {
                                add(castle);
                            }
                        }
                    }, "Two single castles opposite"));
        }

        // 2x Two single castles adjacent
        for (int i = 0; i < 2; i++) {
            tiles.push(new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.FIELD, SideFeature.FIELD,
                    new HashSet<>() {
                        {
                            HashSet<Castle> castles = new HashSet<>() {
                                {
                                    add(new Castle(new ArrayList<CardinalPoint>() {
                                        {
                                            add(CardinalPoint.NNE);
                                            add(CardinalPoint.N);
                                            add(CardinalPoint.NNW);
                                        }
                                    }, false));
                                    add(new Castle(new ArrayList<CardinalPoint>() {
                                        {
                                            add(CardinalPoint.ENE);
                                            add(CardinalPoint.E);
                                            add(CardinalPoint.ESE);
                                        }
                                    }, false));
                                }
                            };
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.SSE);
                                    add(CardinalPoint.S);
                                    add(CardinalPoint.SSW);
                                    add(CardinalPoint.WSW);
                                    add(CardinalPoint.W);
                                    add(CardinalPoint.WNW);
                                }
                            }, castles));
                            for (Castle castle : castles) {
                                add(castle);
                            }
                        }
                    }, "Two single castles adjacent"));
        }

        // 3x Curvy castle with curvy road
        for (int i = 0; i < 3; i++) {
            tiles.push(new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.ROAD, SideFeature.ROAD,
                    new HashSet<>() {
                        {
                            HashSet<Castle> castles = new HashSet<>() {
                                {
                                    add(new Castle(new ArrayList<CardinalPoint>() {
                                        {
                                            add(CardinalPoint.NNW);
                                            add(CardinalPoint.N);
                                            add(CardinalPoint.NNE);
                                            add(CardinalPoint.ENE);
                                            add(CardinalPoint.E);
                                            add(CardinalPoint.ESE);
                                        }
                                    }, false));
                                }
                            };
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.WNW);
                                    add(CardinalPoint.SSE);
                                }
                            }, castles));
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.WSW);
                                    add(CardinalPoint.SSW);
                                }
                            }, castles));
                            add(new Road(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.W);
                                    add(CardinalPoint.S);
                                }
                            }));
                            add(castles.iterator().next());
                        }
                    }, "Curvy castle with curvy road"));
        }

        // 2x Straight castle with shield
        for (int i = 0; i < 2; i++) {
            tiles.push(new Tile(SideFeature.FIELD, SideFeature.CASTLE, SideFeature.FIELD, SideFeature.CASTLE,
                    new HashSet<Feature>() {
                        {
                            HashSet<Castle> castles = new HashSet<Castle>() {
                                {
                                    add(new Castle(new ArrayList<CardinalPoint>() {
                                        {
                                            add(CardinalPoint.WNW);
                                            add(CardinalPoint.W);
                                            add(CardinalPoint.WSW);
                                            add(CardinalPoint.ENE);
                                            add(CardinalPoint.E);
                                            add(CardinalPoint.ESE);
                                        }
                                    }, true));
                                }
                            };
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.NNW);
                                    add(CardinalPoint.N);
                                    add(CardinalPoint.NNE);
                                }
                            }, castles));
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.SSW);
                                    add(CardinalPoint.S);
                                    add(CardinalPoint.SSE);
                                }
                            }, castles));
                            for (Castle castle : castles) {
                                add(castle);
                            }
                        }
                    }, "Straight castle with shield"));
        }

        // 2x Curvy castle with shield
        for (int i = 0; i < 2; i++) {
            tiles.push(new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.FIELD, SideFeature.FIELD,
                    new HashSet<>() {
                        {
                            HashSet<Castle> castles = new HashSet<>() {
                                {
                                    add(new Castle(new ArrayList<CardinalPoint>() {
                                        {
                                            add(CardinalPoint.NNW);
                                            add(CardinalPoint.N);
                                            add(CardinalPoint.NNE);
                                            add(CardinalPoint.ENE);
                                            add(CardinalPoint.E);
                                            add(CardinalPoint.ESE);
                                        }
                                    }, true));
                                }
                            };
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.WNW);
                                    add(CardinalPoint.W);
                                    add(CardinalPoint.WSW);
                                    add(CardinalPoint.SSW);
                                    add(CardinalPoint.S);
                                    add(CardinalPoint.SSE);
                                }
                            }, castles));
                            add(castles.iterator().next());

                        }
                    }, "Curvy castle with shield"));
        }

        // 2x Curvy castle with shield with curvy road
        for (int i = 0; i < 2; i++) {
            tiles.push(new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.ROAD, SideFeature.ROAD,
                    new HashSet<>() {
                        {
                            HashSet<Castle> castles = new HashSet<>() {
                                {
                                    add(new Castle(new ArrayList<CardinalPoint>() {
                                        {
                                            add(CardinalPoint.NNW);
                                            add(CardinalPoint.N);
                                            add(CardinalPoint.NNE);
                                            add(CardinalPoint.ENE);
                                            add(CardinalPoint.E);
                                            add(CardinalPoint.ESE);
                                        }
                                    }, true));
                                }
                            };
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.WNW);
                                    add(CardinalPoint.SSE);
                                }
                            }, castles));
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.WSW);
                                    add(CardinalPoint.SSW);
                                }
                            }, castles));
                            add(new Road(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.W);
                                    add(CardinalPoint.S);
                                }
                            }));
                            add(castles.iterator().next());
                        }
                    }, "Curvy castle with shield with curvy road"));
        }

        // 3x Big castle tiny field
        for (int i = 0; i < 3; i++) {
            tiles.push(new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.FIELD, SideFeature.CASTLE,
                    new HashSet<>() {
                        {
                            HashSet<Castle> castles = new HashSet<>() {
                                {
                                    add(new Castle(new ArrayList<CardinalPoint>() {
                                        {
                                            add(CardinalPoint.NNW);
                                            add(CardinalPoint.N);
                                            add(CardinalPoint.NNE);
                                            add(CardinalPoint.ENE);
                                            add(CardinalPoint.E);
                                            add(CardinalPoint.ESE);
                                            add(CardinalPoint.WSW);
                                            add(CardinalPoint.W);
                                            add(CardinalPoint.WNW);
                                        }
                                    }, false));
                                }
                            };
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.SSE);
                                    add(CardinalPoint.S);
                                    add(CardinalPoint.SSW);
                                }
                            }, castles));
                            add(castles.iterator().next());
                        }
                    }, "Big castle tiny field"));
        }

        // 1x Big castle tiny field with road
        for (int i = 0; i < 1; i++) {
            tiles.push(new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.ROAD, SideFeature.CASTLE,
                    new HashSet<>() {
                        {
                            HashSet<Castle> castles = new HashSet<>() {
                                {
                                    add(new Castle(new ArrayList<CardinalPoint>() {
                                        {
                                            add(CardinalPoint.NNW);
                                            add(CardinalPoint.N);
                                            add(CardinalPoint.NNE);
                                            add(CardinalPoint.ENE);
                                            add(CardinalPoint.E);
                                            add(CardinalPoint.ESE);
                                            add(CardinalPoint.WSW);
                                            add(CardinalPoint.W);
                                            add(CardinalPoint.WNW);
                                        }
                                    }, false));
                                }
                            };
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.SSE);
                                }
                            }, castles));
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.SSW);
                                }
                            }, castles));
                            add(new Road(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.S);
                                }
                            }));
                            add(castles.iterator().next());
                        }
                    }, "Big castle tiny field with road"));
        }

        // 1x Big castle with shield tiny field
        for (int i = 0; i < 1; i++) {
            tiles.push(new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.FIELD, SideFeature.CASTLE,
                    new HashSet<>() {
                        {
                            HashSet<Castle> castles = new HashSet<>() {
                                {
                                    add(new Castle(new ArrayList<CardinalPoint>() {
                                        {
                                            add(CardinalPoint.NNW);
                                            add(CardinalPoint.N);
                                            add(CardinalPoint.NNE);
                                            add(CardinalPoint.ENE);
                                            add(CardinalPoint.E);
                                            add(CardinalPoint.ESE);
                                            add(CardinalPoint.WSW);
                                            add(CardinalPoint.W);
                                            add(CardinalPoint.WNW);
                                        }
                                    }, true));
                                }
                            };
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.SSE);
                                    add(CardinalPoint.S);
                                    add(CardinalPoint.SSW);
                                }
                            }, castles));
                            add(castles.iterator().next());
                        }
                    }, "Big castle with shield tiny field"));
        }
 
        // 2x Big castle with shield tiny field with road
        for (int i = 0; i < 2; i++) {
            tiles.push(new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.ROAD, SideFeature.CASTLE,
                    new HashSet<>() {
                        {
                            HashSet<Castle> castles = new HashSet<>() {
                                {
                                    add(new Castle(new ArrayList<CardinalPoint>() {
                                        {
                                            add(CardinalPoint.NNW);
                                            add(CardinalPoint.N);
                                            add(CardinalPoint.NNE);
                                            add(CardinalPoint.ENE);
                                            add(CardinalPoint.E);
                                            add(CardinalPoint.ESE);
                                            add(CardinalPoint.WSW);
                                            add(CardinalPoint.W);
                                            add(CardinalPoint.WNW);
                                        }
                                    }, true));
                                }
                            };
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.SSE);
                                }
                            }, castles));
                            add(new Field(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.SSW);
                                }
                            }, castles));
                            add(new Road(new ArrayList<CardinalPoint>() {
                                {
                                    add(CardinalPoint.S);
                                }
                            }));
                            add(castles.iterator().next());
                        }
                    }, "Big castle with shield tiny field with road"));
        }

        // 1x Huge castle
        for (int i = 0; i < 1; i++) {
            tiles.push(new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.CASTLE,
                    new HashSet<>() {
                        {
                            HashSet<Castle> castles = new HashSet<>() {
                                {
                                    add(new Castle(new ArrayList<CardinalPoint>() {
                                        {
                                            add(CardinalPoint.NNW);
                                            add(CardinalPoint.N);
                                            add(CardinalPoint.NNE);
                                            add(CardinalPoint.ENE);
                                            add(CardinalPoint.E);
                                            add(CardinalPoint.ESE);
                                            add(CardinalPoint.WSW);
                                            add(CardinalPoint.W);
                                            add(CardinalPoint.WNW);
                                        }
                                    }, false));
                                }
                            };
                            add(castles.iterator().next());
                        }
                    }, "Huge castle"));
        }





        return tiles;
    }
}
