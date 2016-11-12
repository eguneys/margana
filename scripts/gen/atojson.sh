cat anlamlar.txt | sed $s/"/'/g | sed $s/[^*]*/{\n word: &,\n/ | sed $s/\*\*(.*)/ meaning: \1\n},/
