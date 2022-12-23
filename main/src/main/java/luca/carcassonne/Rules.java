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

    public static final Tile MONASTERY = new Tile(SideFeature.FIELD, SideFeature.FIELD, SideFeature.FIELD,
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

    private static final Tile MONASTERY_WITH_ROAD = new Tile(SideFeature.FIELD, SideFeature.FIELD, SideFeature.ROAD,
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

    private static final Tile STRAIGHT_ROAD = new Tile(SideFeature.ROAD, SideFeature.FIELD, SideFeature.ROAD,
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

    private static final Tile CURVY_ROAD = new Tile(SideFeature.ROAD, SideFeature.ROAD, SideFeature.FIELD,
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

    private static final Tile THREE_ROAD_INTERSECTION = new Tile(SideFeature.FIELD, SideFeature.ROAD, SideFeature.ROAD,
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

    private static final Tile FOUR_ROAD_INTERSECTION = new Tile(SideFeature.ROAD, SideFeature.ROAD, SideFeature.ROAD,
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

    private static final Tile SINGLE_CASTLE = new Tile(SideFeature.CASTLE, SideFeature.FIELD, SideFeature.FIELD,
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

    private static final Tile SINGLE_CASTLE_WITH_STRAIGHT_ROAD = new Tile(SideFeature.CASTLE, SideFeature.ROAD,
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

    private static final Tile SINGLE_CASTLE_WITH_CURVY_ROAD_LEFT = new Tile(SideFeature.CASTLE, SideFeature.FIELD,
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

    private static final Tile SINGLE_CASTLE_WITH_CURVY_ROAD_RIGHT = new Tile(SideFeature.CASTLE, SideFeature.ROAD,
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

    private static final Tile SINGLE_CASTLE_WITH_THREE_ROAD_INTERSECTION = new Tile(SideFeature.CASTLE,
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

    private static final Tile LONG_CASTLE = new Tile(SideFeature.FIELD, SideFeature.CASTLE, SideFeature.FIELD,
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

    private static final Tile CURVY_CASTLE = new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.FIELD,
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

    private static final Tile TWO_SINGLE_CASTLES_OPPOSITE = new Tile(SideFeature.CASTLE, SideFeature.FIELD,
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

    private static final Tile TWO_SINGLE_CASTLES_ADJACENT = new Tile(SideFeature.CASTLE, SideFeature.CASTLE,
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

    private static final Tile CURVY_CASTLE_WITH_CURVY_ROAD = new Tile(SideFeature.CASTLE, SideFeature.CASTLE,
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

    private static final Tile STRAIGHT_CASTLE_WITH_SHIELD = new Tile(SideFeature.FIELD, SideFeature.CASTLE,
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

    private static final Tile CURVY_CASTLE_WITH_SHIELD = new Tile(SideFeature.CASTLE, SideFeature.CASTLE,
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

    private static final Tile CURVY_CASTLE_WITH_SHIELD_WITH_CURVY_ROAD = new Tile(SideFeature.CASTLE,
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

    private static final Tile BIG_CASTLE = new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.FIELD,
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

    private static final Tile BIG_CASTLE_WITH_ROAD = new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.ROAD,
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

    private static final Tile BIG_CASTLE_WITH_SHIELD = new Tile(SideFeature.CASTLE, SideFeature.CASTLE,
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

    private static final Tile BIG_CASTLE_WITH_SHIELD_WITH_ROAD = new Tile(SideFeature.CASTLE, SideFeature.CASTLE,
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

    private static final Tile HUGE_CASTLE = new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.CASTLE,
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

    public static final Tile STARTING_TILE = SINGLE_CASTLE_WITH_STRAIGHT_ROAD;

    public static final Stack<Tile> STANDARD_DECK = new Stack<>() {
        {

            // 4x Monasteries
            for (int i = 0; i < N_MONASTERY; i++) {
                push(MONASTERY);
            }

            // 2x Monasteries with road
            for (int i = 0; i < N_MONASTERY_WITH_ROAD; i++) {
                push(MONASTERY_WITH_ROAD);
            }

            // 8x Straight road
            for (int i = 0; i < N_STRAIGHT_ROAD; i++) {
                push(STRAIGHT_ROAD);
            }

            // 9x Curvy road
            for (int i = 0; i < N_CURVY_ROAD; i++) {
                push(CURVY_ROAD);
            }

            // 4x Three road intersection
            for (int i = 0; i < N_THREE_ROAD_INTERSECTION; i++) {
                push(THREE_ROAD_INTERSECTION);
            }

            // 1x Four road intersection
            for (int i = 0; i < N_FOUR_ROAD_INTERSECTION; i++) {
                push(FOUR_ROAD_INTERSECTION);
            }

            // 5x Single castle
            for (int i = 0; i < N_SINGLE_CASTLE; i++) {
                push(SINGLE_CASTLE);
            }

            // 4x Single castle with straight road
            for (int i = 0; i < N_SINGLE_CASTLE_WITH_STRAIGHT_ROAD; i++) {
                push(SINGLE_CASTLE_WITH_STRAIGHT_ROAD);
            }

            // 3x Single castle with curvy road left
            for (int i = 0; i < N_SINGLE_CASTLE_WITH_CURVY_ROAD_LEFT; i++) {
                push(SINGLE_CASTLE_WITH_CURVY_ROAD_LEFT);
            }

            // 3x Single castle with curvy road right
            for (int i = 0; i < N_SINGLE_CASTLE_WITH_CURVY_ROAD_RIGHT; i++) {
                push(SINGLE_CASTLE_WITH_CURVY_ROAD_RIGHT);
            }

            // 3x Single castle with three road intersection
            for (int i = 0; i < N_SINGLE_CASTLE_WITH_THREE_ROAD_INTERSECTION; i++) {
                push(SINGLE_CASTLE_WITH_THREE_ROAD_INTERSECTION);

            }

            // 1x Long castle
            for (int i = 0; i < N_LONG_CASTLE; i++) {
                push(LONG_CASTLE);
            }

            // 3x Curvy castle
            for (int i = 0; i < N_CURVY_CASTLE; i++) {
                push(CURVY_CASTLE);
            }

            // 3x Two single castles opposite
            for (int i = 0; i < N_TWO_SINGLE_CASTLES_OPPOSITE; i++) {
                push(TWO_SINGLE_CASTLES_OPPOSITE);
            }

            // 2x Two single castles adjacent
            for (int i = 0; i < N_TWO_SINGLE_CASTLES_ADJACENT; i++) {
                push(TWO_SINGLE_CASTLES_ADJACENT);
            }

            // 3x Curvy castle with curvy road
            for (int i = 0; i < N_CURVY_CASTLE_WITH_CURVY_ROAD; i++) {
                push(CURVY_CASTLE_WITH_CURVY_ROAD);
            }

            // 2x Straight castle with shield
            for (int i = 0; i < N_STRAIGHT_CASTLE_WITH_SHIELD; i++) {
                push(STRAIGHT_CASTLE_WITH_SHIELD);
            }

            // 2x Curvy castle with shield
            for (int i = 0; i < N_CURVY_CASTLE_WITH_SHIELD; i++) {
                push(CURVY_CASTLE_WITH_SHIELD);
            }

            // 2x Curvy castle with shield with curvy road
            for (int i = 0; i < N_CURVY_CASTLE_WITH_SHIELD_WITH_CURVY_ROAD; i++) {
                push(CURVY_CASTLE_WITH_SHIELD_WITH_CURVY_ROAD);
            }

            // 3x Big castle
            for (int i = 0; i < N_BIG_CASTLE; i++) {
                push(BIG_CASTLE);
            }

            // 1x Big castle with road
            for (int i = 0; i < N_BIG_CASTLE_WITH_ROAD; i++) {
                push(BIG_CASTLE_WITH_ROAD);
            }

            // 1x Big castle with shield
            for (int i = 0; i < N_BIG_CASTLE_WITH_SHIELD; i++) {
                push(BIG_CASTLE_WITH_SHIELD);
            }

            // 2x Big castle with shield with road
            for (int i = 0; i < N_BIG_CASTLE_WITH_SHIELD_WITH_ROAD; i++) {
                push(BIG_CASTLE_WITH_SHIELD_WITH_ROAD);
            }

            // 1x Huge castle
            for (int i = 0; i < N_HUGE_CASTLE; i++) {
                push(HUGE_CASTLE);
            }
        }
    };
}