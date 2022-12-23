package luca.carcassonne;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class Rules {
    // Feature score constants
    public static final int ROAD_POINTS_OPEN = 1;
    public static final int ROAD_POINTS_CLOSED = 1;
    public static final int CASTLE_POINTS_OPEN = 1;
    public static final int CASTLE_POINTS_CLOSED = 2;
    public static final int SHIELD_POINTS_OPEN = 1;
    public static final int SHIELD_POINTS_CLOSED = 2;
    public static final int FIELD_POINTS_PER_CASTLE = 3;

    // Tile number constants
    private static final int MONASTERY = 4;
    private static final int MONASTERY_WITH_ROAD = 2;
    private static final int STRAIGHT_ROAD = 8;
    private static final int CURVY_ROAD = 9;
    private static final int THREE_ROAD_INTERSECTION = 4;
    private static final int FOUR_ROAD_INTERSECTION = 1;
    private static final int SINGLE_CASTLE = 5;
    private static final int SINGLE_CASTLE_WITH_STRAIGHT_ROAD = 4;
    private static final int SINGLE_CASTLE_WITH_CURVY_ROAD_LEFT = 3;
    private static final int SINGLE_CASTLE_WITH_CURVY_ROAD_RIGHT = 3;
    private static final int SINGLE_CASTLE_WITH_THREE_ROAD_INTERSECTION = 3;
    private static final int LONG_CASTLE = 1;
    private static final int CURVY_CASTLE = 3;
    private static final int TWO_SINGLE_CASTLES_OPPOSITE = 3;
    private static final int TWO_SINGLE_CASTLES_ADJACENT = 3;
    private static final int CURVY_CASTLE_WITH_CURVY_ROAD = 3;
    private static final int STRAIGHT_CASTLE_WITH_SHIELD = 2;
    private static final int CURVY_CASTLE_WITH_SHIELD = 2;
    private static final int CURVY_CASTLE_WITH_SHIELD_WITH_CURVY_ROAD = 2;
    private static final int BIG_CASTLE = 3;
    private static final int BIG_CASTLE_WITH_ROAD = 1;
    private static final int BIG_CASTLE_WITH_SHIELD = 1;
    private static final int BIG_CASTLE_WITH_SHIELD_WITH_ROAD = 2;
    private static final int HUGE_CASTLE = 1;
    


    public static final Stack<Tile> STANDARD_DECK = new Stack<>() {
        {

            // 4x Monasteries
            for (int i = 0; i < MONASTERY; i++) {
                push(new Tile(SideFeature.FIELD, SideFeature.FIELD, SideFeature.FIELD, SideFeature.FIELD,
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
            for (int i = 0; i < MONASTERY_WITH_ROAD; i++) {
                push(new Tile(SideFeature.FIELD, SideFeature.FIELD, SideFeature.ROAD, SideFeature.FIELD,
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
            for (int i = 0; i < STRAIGHT_ROAD; i++) {
                push(new Tile(SideFeature.ROAD, SideFeature.FIELD, SideFeature.ROAD, SideFeature.FIELD,
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
            for (int i = 0; i < CURVY_ROAD; i++) {
                push(new Tile(SideFeature.ROAD, SideFeature.ROAD, SideFeature.FIELD, SideFeature.FIELD,
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
            for (int i = 0; i < THREE_ROAD_INTERSECTION; i++) {
                push(new Tile(SideFeature.FIELD, SideFeature.ROAD, SideFeature.ROAD, SideFeature.ROAD,
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
            for (int i = 0; i < FOUR_ROAD_INTERSECTION; i++) {
                push(new Tile(SideFeature.ROAD, SideFeature.ROAD, SideFeature.ROAD, SideFeature.ROAD,
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
            for (int i = 0; i < SINGLE_CASTLE; i++) {
                push(new Tile(SideFeature.CASTLE, SideFeature.FIELD, SideFeature.FIELD, SideFeature.FIELD,
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
            for (int i = 0; i < SINGLE_CASTLE_WITH_STRAIGHT_ROAD; i++) {
                push(new Tile(SideFeature.CASTLE, SideFeature.ROAD, SideFeature.FIELD, SideFeature.ROAD,
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
            for (int i = 0; i < SINGLE_CASTLE_WITH_CURVY_ROAD_LEFT; i++) {
                push(new Tile(SideFeature.CASTLE, SideFeature.FIELD, SideFeature.ROAD, SideFeature.ROAD,
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
            for (int i = 0; i < SINGLE_CASTLE_WITH_CURVY_ROAD_RIGHT; i++) {
                push(new Tile(SideFeature.CASTLE, SideFeature.ROAD, SideFeature.ROAD, SideFeature.FIELD,
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
            for (int i = 0; i < SINGLE_CASTLE_WITH_THREE_ROAD_INTERSECTION; i++) {
                push(new Tile(SideFeature.CASTLE, SideFeature.ROAD, SideFeature.ROAD, SideFeature.ROAD,
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
            for (int i = 0; i < LONG_CASTLE; i++) {
                push(new Tile(SideFeature.FIELD, SideFeature.CASTLE, SideFeature.FIELD, SideFeature.CASTLE,
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
            for (int i = 0; i < CURVY_CASTLE; i++) {
                push(new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.FIELD, SideFeature.FIELD,
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
            for (int i = 0; i < TWO_SINGLE_CASTLES_OPPOSITE; i++) {
                push(new Tile(SideFeature.CASTLE, SideFeature.FIELD, SideFeature.CASTLE, SideFeature.FIELD,
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
            for (int i = 0; i < TWO_SINGLE_CASTLES_ADJACENT; i++) {
                push(new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.FIELD, SideFeature.FIELD,
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
            for (int i = 0; i < CURVY_CASTLE_WITH_CURVY_ROAD; i++) {
                push(new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.ROAD, SideFeature.ROAD,
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
            for (int i = 0; i < STRAIGHT_CASTLE_WITH_SHIELD; i++) {
                push(new Tile(SideFeature.FIELD, SideFeature.CASTLE, SideFeature.FIELD, SideFeature.CASTLE,
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
            for (int i = 0; i < CURVY_CASTLE_WITH_SHIELD; i++) {
                push(new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.FIELD, SideFeature.FIELD,
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
            for (int i = 0; i < CURVY_CASTLE_WITH_SHIELD_WITH_CURVY_ROAD; i++) {
                push(new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.ROAD, SideFeature.ROAD,
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

            // 3x Big castle
            for (int i = 0; i < BIG_CASTLE; i++) {
                push(new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.FIELD, SideFeature.CASTLE,
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
                        }, "Big castle"));
            }

            // 1x Big castle with road
            for (int i = 0; i < BIG_CASTLE_WITH_ROAD; i++) {
                push(new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.ROAD, SideFeature.CASTLE,
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
                        }, "Big castle with road"));
            }

            // 1x Big castle with shield
            for (int i = 0; i < BIG_CASTLE_WITH_SHIELD; i++) {
                push(new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.FIELD, SideFeature.CASTLE,
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
                        }, "Big castle with shield"));
            }

            // 2x Big castle with shield with road
            for (int i = 0; i < BIG_CASTLE_WITH_SHIELD_WITH_ROAD; i++) {
                push(new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.ROAD, SideFeature.CASTLE,
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
                        }, "Big castle with shield with road"));
            }

            // 1x Huge castle
            for (int i = 0; i < HUGE_CASTLE; i++) {
                push(new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.CASTLE,
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

        }
    };
}