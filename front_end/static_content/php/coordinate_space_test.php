<?php

enum Coordinate_Corner {
    case UpperRight;
    case UpperLeft;
    case DownRight;
    case DownLeft;
}

class Coordinate_Space_Test {

    public static function isPointHitting(int $x, int $y, int $r): bool {
        $corner = Coordinate_Space_Test::getCoordinateCorner($x, $y);

        switch ($corner) {
            case Coordinate_Corner::UpperRight:

                return false;
            case Coordinate_Corner::UpperLeft:
                
                return $x**2+ $y**2<= $r**2;
            case Coordinate_Corner::DownRight:

                return $y >= $x - $r/2;
            case Coordinate_Corner::DownLeft:
                
                return $x >= -$r/2 && -$r < $y;
        }

        return false;    
    }

    public static function getCoordinateCorner(int $x, int $y): Coordinate_Corner {

        if ($x>=0) {
            if ($y>=0) {
                return Coordinate_Corner::UpperRight;
            } else {
                return Coordinate_Corner::DownRight;
            }
        } else {
            if ($y>=0) {
                return Coordinate_Corner::UpperLeft;
            } else {
                return Coordinate_Corner::DownLeft;
            }
        }
    }
}

?>