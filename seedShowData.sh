#!/bin/bash
MOVIE_IDS=('1' '2' '3')

declare -a slot_ids
declare start_date
declare next_date

DB_HOST='localhost'
DB_DATABASE="${POSTGRES_DB}"
DB_USERNAME="${POSTGRES_USER}"

OS_TYPE=$(uname -s)

number_of_weeks=$2

get_random_movie_id () {
  lower_index=0
  upper_index=$(( ${#MOVIE_IDS[@]} - 1 ))
  echo ${MOVIE_IDS[$(( $RANDOM % (( $upper_index - $lower_index + 1 )) + $lower_index ))]}
}

get_random_price_with_two_decimal_places () {
  price_lower_value=150
  price_upper_value=300
  echo $(( $RANDOM % (( $price_upper_value - $price_lower_value + 1 )) + $price_lower_value )).$(( RANDOM % 99 ))
}

get_slot_ids_from_db () {
  slot_ids=($(PGPASSWORD=${DB_PASSWORD} psql -h ${DB_HOST} -U ${DB_USERNAME} -d ${DB_DATABASE} -qtc "select id from slot"))
}

initialise_dates () {
  if [ "$OS_TYPE" == "Darwin" ]
  then
    start_date=$(date -j -f %Y-%m-%d $1 +%Y-%m-%d)
  else
    start_date=$(date --date "$1" "+%Y-%m-%d")
  fi
  next_date=${start_date}
}

get_next_date () {
  if [ "$OS_TYPE" == "Darwin" ]
  then
    next_date_command="date -j -f %Y-%m-%d -v+1d $1 +%Y-%m-%d"
  else
    next_date_command="date --date \"$1 +1 day\" \"+%Y-%m-%d\""
  fi
  echo $(eval $next_date_command)
}

clear_old_data () {
  echo "Truncating the following tables in database: booking, show, slot..."

  PGPASSWORD=${DB_PASSWORD} psql -h ${DB_HOST} -U ${DB_USERNAME} -d ${DB_DATABASE} -qc "truncate booking, show, slot";

  echo "Tables successfully truncated!"
}

seed_data_for_first_day () {
  for ((slot_id = ${slot_ids[0]}; slot_id <= ${slot_ids[ ${#slot_ids[@]} - 1 ]}; slot_id++));
  do
    movie_id=$( get_random_movie_id )
    price=$( get_random_price_with_two_decimal_places )
    PGPASSWORD=${DB_PASSWORD} psql -h ${DB_HOST} -U ${DB_USERNAME} -d ${DB_DATABASE} -qc \
    "insert into show (movie_id, date, slot_id, cost) values ('$movie_id', '${start_date}', $slot_id, $price)"
  done
}

seed_data_second_day_onwards () {
  next_date=$( get_next_date "$start_date" )
  for ((day = 2; day <= $1; day++));
  do
    for ((slot_id = ${slot_ids[0]}; slot_id <= ${slot_ids[ ${#slot_ids[@]} - 1 ]}; slot_id++));
    do
      movie_id=$( get_random_movie_id )
      price=$( get_random_price_with_two_decimal_places )
      PGPASSWORD=${DB_PASSWORD} psql -h ${DB_HOST} -U ${DB_USERNAME} -d ${DB_DATABASE} -qc \
      "insert into show (movie_id, date, slot_id, cost) values ('$movie_id', '$next_date', $slot_id, $price)"
    done
    next_date=$( get_next_date "$next_date" )
  done
}

seed_slot_data () {
  echo "Seeding slot data in database..."

  PGPASSWORD=${DB_PASSWORD} psql -h ${DB_HOST} -U ${DB_USERNAME} -d ${DB_DATABASE} -qc \
  "insert into slot (name, start_time, end_time) values \
  ('slot1', '09:00:00', '12:30:00'), \
  ('slot2', '13:30:00', '17:00:00'), \
  ('slot3', '18:00:00', '21:30:00'), \
  ('slot4', '22:30:00', '02:00:00')";

  echo "Slot data successfully seeded!"
}

seed_show_data () {
  echo "Seeding show data in database..."

  number_of_days=$(( ${number_of_weeks} * 7 ))
  get_slot_ids_from_db

  if [ $number_of_days -ne 0 ]
  then
    seed_data_for_first_day
    seed_data_second_day_onwards "$number_of_days"
  fi

  echo "Show data successfully seeded!"
}

initialise_dates "$1"
clear_old_data
seed_slot_data
seed_show_data
