var array_length = 10;
var tableSize = 10;
var startIndex = 1;
var endIndex = 10;
var currentIndex = 1;
var maxIndex = 3;

$(document).ready(function() {
    function displayIndexButton() {
        $(".indexBtns button").remove();
        $(".indexBtns").append('<button>Previous</button>');

        for (let i = 1; i <= maxIndex; i++) {
            $(".indexBtns").append('<button index="'+i+'">'+i+'</button>');
        }
        $(".indexBtns").append('<button>Next</button>');
    }

    displayIndexButton();
});

