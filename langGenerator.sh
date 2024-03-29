#
# Copyright (c) 2021 juancarloscp52
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>
#
#!/bin/bash
cd src/main/resources/assets/entropy/lang
declare -a spanish=("ar cl ec mx uy ve")
for elem in $spanish; do
    echo $elem
    cp es_es.json es_$elem.json
done

declare -a german=("at ch")
for elem in $german; do
    echo $elem
    cp de_de.json de_$elem.json
done