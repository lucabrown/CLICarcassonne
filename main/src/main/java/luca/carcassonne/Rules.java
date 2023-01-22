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
    private static final int N_MONASTERY = 4;
    private static final int N_MONASTERY_WITH_ROAD = 2;
    private static final int N_STRAIGHT_ROAD = 8;
    private static final int N_CURVY_ROAD = 9;
    private static final int N_THREE_ROAD_INTERSECTION = 4;
    private static final int N_FOUR_ROAD_INTERSECTION = 1;
    private static final int N_SINGLE_CASTLE = 5;
    private static final int N_SINGLE_CASTLE_WITH_STRAIGHT_ROAD = 4;
    private static final int N_SINGLE_CASTLE_WITH_CURVY_ROAD_LEFT = 3;
    private static final int N_SINGLE_CASTLE_WITH_CURVY_ROAD_RIGHT = 3;
    private static final int N_SINGLE_CASTLE_WITH_THREE_ROAD_INTERSECTION = 3;
    private static final int N_LONG_CASTLE = 1;
    private static final int N_CURVY_CASTLE = 3;
    private static final int N_TWO_SINGLE_CASTLES_OPPOSITE = 3;
    private static final int N_TWO_SINGLE_CASTLES_ADJACENT = 3;
    private static final int N_CURVY_CASTLE_WITH_CURVY_ROAD = 3;
    private static final int N_STRAIGHT_CASTLE_WITH_SHIELD = 2;
    private static final int N_CURVY_CASTLE_WITH_SHIELD = 2;
    private static final int N_CURVY_CASTLE_WITH_SHIELD_WITH_CURVY_ROAD = 2;
    private static final int N_BIG_CASTLE = 3;
    private static final int N_BIG_CASTLE_WITH_ROAD = 1;
    private static final int N_BIG_CASTLE_WITH_SHIELD = 1;
    private static final int N_BIG_CASTLE_WITH_SHIELD_WITH_ROAD = 2;
    private static final int N_HUGE_CASTLE = 1;

    public static final Tile getMonastery() {
        return new Tile(SideFeature.FIELD, SideFeature.FIELD, SideFeature.FIELD,
                SideFeature.FIELD,
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
                }, "Monastery");
    }

    public static final Tile getMonasteryWithRoad() {
        return new Tile(SideFeature.FIELD, SideFeature.FIELD, SideFeature.ROAD,
                SideFeature.FIELD,
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
                }, "Monastery with road");
    }

    public static final Tile getStraightRoad() {
        return new Tile(SideFeature.ROAD, SideFeature.FIELD, SideFeature.ROAD,
                SideFeature.FIELD,
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
                }, "Straight road");
    }

    public static final Tile getCurvyRoad() {
        return new Tile(SideFeature.ROAD, SideFeature.ROAD, SideFeature.FIELD,
                SideFeature.FIELD,
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
                }, "Curvy road");
    }

    public static final Tile getThreeRoadIntersection() {
        return new Tile(SideFeature.FIELD, SideFeature.ROAD, SideFeature.ROAD,
                SideFeature.ROAD,
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
                }, "Three road intersection");
    }

    public static final Tile getFourRoadIntersection() {
        return new Tile(SideFeature.ROAD, SideFeature.ROAD, SideFeature.ROAD,
                SideFeature.ROAD,
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
                }, "Four road intersection");
    }

    public static final Tile getSingleCastle() {
        return new Tile(SideFeature.CASTLE, SideFeature.FIELD, SideFeature.FIELD,
                SideFeature.FIELD,
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
                }, "Single castle");
    }

    public static final Tile getSingleCastleWithStraightRoad() {
        return new Tile(SideFeature.CASTLE, SideFeature.ROAD,
                SideFeature.FIELD, SideFeature.ROAD,
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
                }, "Single castle with straight road");
    }

    public static final Tile getSingleCastleWithCurvyRoadLeft() {
        return new Tile(SideFeature.CASTLE, SideFeature.FIELD,
                SideFeature.ROAD, SideFeature.ROAD,
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
                }, "Single castle with curvy road left");
    }

    public static final Tile getSingleCastleWithCurvyRoadRight() {
        return new Tile(SideFeature.CASTLE, SideFeature.ROAD,
                SideFeature.ROAD, SideFeature.FIELD,
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
                }, "Single castle with curvy road right");
    }

    public static final Tile getSingleCastleWithThreeRoadIntersection() {
        return new Tile(SideFeature.CASTLE,
                SideFeature.ROAD, SideFeature.ROAD, SideFeature.ROAD,
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
                }, "Single castle with three road intersection");
    }

    public static final Tile getLongCastle() {
        return new Tile(SideFeature.FIELD, SideFeature.CASTLE, SideFeature.FIELD,
                SideFeature.CASTLE,
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
                }, "Long castle");
    }

    public static final Tile getCurvyCastle() {
        return new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.FIELD,
                SideFeature.FIELD,
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
                }, "Curvy castle");
    }

    public static final Tile getTwoSingleCastlesOpposite() {
        return new Tile(SideFeature.CASTLE, SideFeature.FIELD,
                SideFeature.CASTLE, SideFeature.FIELD,
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
                }, "Two single castles opposite");
    }

    public static final Tile getTwoSingleCastlesAdjacent() {
        return new Tile(SideFeature.CASTLE, SideFeature.CASTLE,
                SideFeature.FIELD, SideFeature.FIELD,
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
                }, "Two single castles adjacent");
    }

    public static final Tile getCurvyCastleWithCurvyRoad() {
        return new Tile(SideFeature.CASTLE, SideFeature.CASTLE,
                SideFeature.ROAD, SideFeature.ROAD,
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
                }, "Curvy castle with curvy road");
    }

    public static final Tile getStraightCastleWithShield() {
        return new Tile(SideFeature.FIELD, SideFeature.CASTLE,
                SideFeature.FIELD, SideFeature.CASTLE,
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
                }, "Straight castle with shield");
    }

    public static final Tile getCurvyCastleWithShield() {
        return new Tile(SideFeature.CASTLE, SideFeature.CASTLE,
                SideFeature.FIELD, SideFeature.FIELD,
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
                }, "Curvy castle with shield");
    }

    public static final Tile getCurvyCastleWithShieldWithCurvyRoad() {
        return new Tile(SideFeature.CASTLE,
                SideFeature.CASTLE, SideFeature.ROAD, SideFeature.ROAD,
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
                }, "Curvy castle with shield with curvy road");
    }

    public static final Tile getBigCastle() {
        return new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.FIELD,
                SideFeature.CASTLE,
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
                }, "Big castle");
    }

    public static final Tile getBigCastleWithRoad() {
        return new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.ROAD,
                SideFeature.CASTLE,
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
                }, "Big castle with road");
    }

    public static final Tile getBigCastleWithShield() {
        return new Tile(SideFeature.CASTLE, SideFeature.CASTLE,
                SideFeature.FIELD, SideFeature.CASTLE,
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
                }, "Big castle with shield");
    }

    public static final Tile getBigCastleWithShieldWithRoad() {
        return new Tile(SideFeature.CASTLE, SideFeature.CASTLE,
                SideFeature.ROAD, SideFeature.CASTLE,
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
                }, "Big castle with shield with road");
    }

    public static final Tile getHugeCastle() {
        return new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.CASTLE,
                SideFeature.CASTLE,
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
                                        add(CardinalPoint.SSE);
                                        add(CardinalPoint.S);
                                        add(CardinalPoint.SSW);
                                        add(CardinalPoint.WSW);
                                        add(CardinalPoint.W);
                                        add(CardinalPoint.WNW);
                                    }
                                }, false));
                            }
                        };
                        add(castles.iterator().next());
                    }
                }, "Huge castle");
    }

    public static final Tile getStartingTile() {
        return getSingleCastleWithStraightRoad();
    }

    public static final Stack<Tile> getMonasteryDeck(){
        return new Stack<>() {
            {
                for(int i = 0; i < 72; i++){
                    push(getMonastery());
                }
            }
        };
    }

    public static final Stack<Tile> getStandardDeck() {
        return new Stack<>() {
            {

                // 4x Monasteries
                for (int i = 0; i < N_MONASTERY; i++) {
                    push(getMonastery());
                }

                // 2x Monasteries with road
                for (int i = 0; i < N_MONASTERY_WITH_ROAD; i++) {
                    push(getMonasteryWithRoad());
                }

                // 8x Straight road
                for (int i = 0; i < N_STRAIGHT_ROAD; i++) {
                    push(getStraightRoad());
                }

                // 9x Curvy road
                for (int i = 0; i < N_CURVY_ROAD; i++) {
                    push(getCurvyRoad());
                }

                // 4x Three road intersection
                for (int i = 0; i < N_THREE_ROAD_INTERSECTION; i++) {
                    push(getThreeRoadIntersection());
                }

                // 1x Four road intersection
                for (int i = 0; i < N_FOUR_ROAD_INTERSECTION; i++) {
                    push(getFourRoadIntersection());
                }

                // 5x Single castle
                for (int i = 0; i < N_SINGLE_CASTLE; i++) {
                    push(getSingleCastle());
                }

                // 4x Single castle with straight road
                for (int i = 0; i < N_SINGLE_CASTLE_WITH_STRAIGHT_ROAD; i++) {
                    push(getSingleCastleWithStraightRoad());
                }

                // 3x Single castle with curvy road left
                for (int i = 0; i < N_SINGLE_CASTLE_WITH_CURVY_ROAD_LEFT; i++) {
                    push(getSingleCastleWithCurvyRoadLeft());
                }

                // 3x Single castle with curvy road right
                for (int i = 0; i < N_SINGLE_CASTLE_WITH_CURVY_ROAD_RIGHT; i++) {
                    push(getSingleCastleWithCurvyRoadRight());
                }

                // 3x Single castle with three road intersection
                for (int i = 0; i < N_SINGLE_CASTLE_WITH_THREE_ROAD_INTERSECTION; i++) {
                    push(getSingleCastleWithThreeRoadIntersection());
                }

                // 1x Long castle
                for (int i = 0; i < N_LONG_CASTLE; i++) {
                    push(getLongCastle());
                }

                // 3x Curvy castle
                for (int i = 0; i < N_CURVY_CASTLE; i++) {
                    push(getCurvyCastle());
                }

                // 3x Two single castles opposite
                for (int i = 0; i < N_TWO_SINGLE_CASTLES_OPPOSITE; i++) {
                    push(getTwoSingleCastlesOpposite());
                }

                // 2x Two single castles adjacent
                for (int i = 0; i < N_TWO_SINGLE_CASTLES_ADJACENT; i++) {
                    push(getTwoSingleCastlesAdjacent());
                }

                // 3x Curvy castle with curvy road
                for (int i = 0; i < N_CURVY_CASTLE_WITH_CURVY_ROAD; i++) {
                    push(getCurvyCastleWithCurvyRoad());
                }

                // 2x Straight castle with shield
                for (int i = 0; i < N_STRAIGHT_CASTLE_WITH_SHIELD; i++) {
                    push(getStraightCastleWithShield());
                }

                // 2x Curvy castle with shield
                for (int i = 0; i < N_CURVY_CASTLE_WITH_SHIELD; i++) {
                    push(getCurvyCastleWithShield());
                }

                // 2x Curvy castle with shield with curvy road
                for (int i = 0; i < N_CURVY_CASTLE_WITH_SHIELD_WITH_CURVY_ROAD; i++) {
                    push(getCurvyCastleWithShieldWithCurvyRoad());
                }

                // 3x Big castle
                for (int i = 0; i < N_BIG_CASTLE; i++) {
                    push(getBigCastle());
                }

                // 1x Big castle with road
                for (int i = 0; i < N_BIG_CASTLE_WITH_ROAD; i++) {
                    push(getBigCastleWithRoad());
                }

                // 1x Big castle with shield
                for (int i = 0; i < N_BIG_CASTLE_WITH_SHIELD; i++) {
                    push(getBigCastleWithShield());
                }

                // 2x Big castle with shield with road
                for (int i = 0; i < N_BIG_CASTLE_WITH_SHIELD_WITH_ROAD; i++) {
                    push(getBigCastleWithShieldWithRoad());
                }

                // 1x Huge castle
                for (int i = 0; i < N_HUGE_CASTLE; i++) {
                    push(getHugeCastle());
                }
            }
        };
    }
}