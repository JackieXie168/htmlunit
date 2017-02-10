// Copyright 2012 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @fileoverview Implementation of AES in JavaScript.
 * @see http://en.wikipedia.org/wiki/Advanced_Encryption_Standard
 *
 * @author nnaze@google.com (Nathan Naze) - port to Closure
 */

goog.provide('goog.crypt.Aes');

goog.require('goog.asserts');
goog.require('goog.crypt.BlockCipher');



/**
 * Implementation of AES in JavaScript.
 * See http://en.wikipedia.org/wiki/Advanced_Encryption_Standard
 *
 * WARNING: This is ECB mode only. If you are encrypting something
 * longer than 16 bytes, or encrypting more than one value with the same key
 * (so basically, always) you need to use this with a block cipher mode of
 * operation.  See goog.crypt.Cbc.
 *
 * See http://en.wikipedia.org/wiki/Block_cipher_modes_of_operation for more
 * information.
 *
 * @constructor
 * @implements {goog.crypt.BlockCipher}
 * @param {!Array<number>} key The key as an array of integers in {0, 255}.
 *     The key must have lengths of 16, 24, or 32 integers for 128-,
 *     192-, or 256-bit encryption, respectively.
 * @final
 * @struct
 */
goog.crypt.Aes = function(key) {
  goog.crypt.Aes.assertKeyArray_(key);

  /**
   * The AES key.
   * @type {!Array<number>}
   * @private
   */
  this.key_ = key;

  /**
   * Key length, in words.
   * @type {number}
   * @private
   */
  this.keyLength_ = this.key_.length / 4;

  /**
   * Number of rounds.  Based on key length per AES spec.
   * @type {number}
   * @private
   */
  this.numberOfRounds_ = this.keyLength_ + 6;

  /**
   * 4x4 byte array containing the current state.
   * @type {!Array<!Array<number>>}
   * @private
   */
  this.state_ = [[], [], [], []];

  /**
   * Scratch temporary array for calculation.
   * @type {!Array<!Array<number>>}
   * @private
   */
  this.temp_ = [[], [], [], []];

  /**
   * The key schedule.
   * @type {!Array<!Array<number>>}
   * @private
   */
  this.keySchedule_;

  this.keyExpansion_();
};


/**
 * @define {boolean} Whether to call test method stubs.  This can be enabled
 *     for unit testing.
 */
goog.define('goog.crypt.Aes.ENABLE_TEST_MODE', false);


/**
 * @override
 */
goog.crypt.Aes.prototype.encrypt = function(input) {

  if (goog.crypt.Aes.ENABLE_TEST_MODE) {
    this.testKeySchedule_(0, this.keySchedule_, 0);
  }

  this.copyInput_(input);
  this.addRoundKey_(0);

  for (var round = 1; round < this.numberOfRounds_; ++round) {
    if (goog.crypt.Aes.ENABLE_TEST_MODE) {
      this.testKeySchedule_(round, this.keySchedule_, round);
      this.testStartRound_(round, this.state_);
    }

    this.subBytes_(goog.crypt.Aes.SBOX_);
    if (goog.crypt.Aes.ENABLE_TEST_MODE) {
      this.testAfterSubBytes_(round, this.state_);
    }

    this.shiftRows_();
    if (goog.crypt.Aes.ENABLE_TEST_MODE) {
      this.testAfterShiftRows_(round, this.state_);
    }

    this.mixColumns_();
    if (goog.crypt.Aes.ENABLE_TEST_MODE) {
      this.testAfterMixColumns_(round, this.state_);
    }

    this.addRoundKey_(round);
  }

  this.subBytes_(goog.crypt.Aes.SBOX_);
  if (goog.crypt.Aes.ENABLE_TEST_MODE) {
    this.testAfterSubBytes_(round, this.state_);
  }

  this.shiftRows_();
  if (goog.crypt.Aes.ENABLE_TEST_MODE) {
    this.testAfterShiftRows_(round, this.state_);
  }

  this.addRoundKey_(this.numberOfRounds_);

  return this.generateOutput_();
};


/**
 * @override
 */
goog.crypt.Aes.prototype.decrypt = function(input) {

  if (goog.crypt.Aes.ENABLE_TEST_MODE) {
    this.testKeySchedule_(0, this.keySchedule_, this.numberOfRounds_);
  }

  this.copyInput_(input);
  this.addRoundKey_(this.numberOfRounds_);

  for (var round = 1; round < this.numberOfRounds_; ++round) {
    if (goog.crypt.Aes.ENABLE_TEST_MODE) {
      this.testKeySchedule_(
          round, this.keySchedule_, this.numberOfRounds_ - round);
      this.testStartRound_(round, this.state_);
    }

    this.invShiftRows_();
    if (goog.crypt.Aes.ENABLE_TEST_MODE) {
      this.testAfterShiftRows_(round, this.state_);
    }

    this.subBytes_(goog.crypt.Aes.INV_SBOX_);
    if (goog.crypt.Aes.ENABLE_TEST_MODE) {
      this.testAfterSubBytes_(round, this.state_);
    }

    this.addRoundKey_(this.numberOfRounds_ - round);
    if (goog.crypt.Aes.ENABLE_TEST_MODE) {
      this.testAfterAddRoundKey_(round, this.state_);
    }

    this.invMixColumns_();
  }

  this.invShiftRows_();
  if (goog.crypt.Aes.ENABLE_TEST_MODE) {
    this.testAfterShiftRows_(round, this.state_);
  }

  this.subBytes_(goog.crypt.Aes.INV_SBOX_);
  if (goog.crypt.Aes.ENABLE_TEST_MODE) {
    this.testAfterSubBytes_(this.numberOfRounds_, this.state_);
  }

  if (goog.crypt.Aes.ENABLE_TEST_MODE) {
    this.testKeySchedule_(this.numberOfRounds_, this.keySchedule_, 0);
  }

  this.addRoundKey_(0);

  return this.generateOutput_();
};


/**
 * Block size, in words.  Fixed at 4 per AES spec.
 * @type {number}
 * @private
 */
goog.crypt.Aes.BLOCK_SIZE_ = 4;


/**
 * Asserts that the key's array of integers is in the correct format.
 * @param {!Array<number>} arr AES key as array of integers.
 * @private
 */
goog.crypt.Aes.assertKeyArray_ = function(arr) {
  if (goog.asserts.ENABLE_ASSERTS) {
    goog.asserts.assert(
        arr.length == 16 || arr.length == 24 || arr.length == 32,
        'Key must have length 16, 24, or 32.');
    for (var i = 0; i < arr.length; i++) {
      goog.asserts.assertNumber(arr[i]);
      goog.asserts.assert(arr[i] >= 0 && arr[i] <= 255);
    }
  }
};


/**
 * Tests can populate this with a callback, and that callback will get called
 * at the start of each round *in both functions encrypt() and decrypt()*.
 * @param {number} roundNum Round number.
 * @param {!Array<Array<number>>} Current state.
 * @private
 */
goog.crypt.Aes.prototype.testStartRound_ = goog.nullFunction;


/**
 * Tests can populate this with a callback, and that callback will get called
 * each round right after the SubBytes step gets executed *in both functions
 * encrypt() and decrypt()*.
 * @param {number} roundNum Round number.
 * @param {!Array<Array<number>>} Current state.
 * @private
 */
goog.crypt.Aes.prototype.testAfterSubBytes_ = goog.nullFunction;


/**
 * Tests can populate this with a callback, and that callback will get called
 * each round right after the ShiftRows step gets executed *in both functions
 * encrypt() and decrypt()*.
 * @param {number} roundNum Round number.
 * @param {!Array<Array<number>>} Current state.
 * @private
 */
goog.crypt.Aes.prototype.testAfterShiftRows_ = goog.nullFunction;


/**
 * Tests can populate this with a callback, and that callback will get called
 * each round right after the MixColumns step gets executed *but only in the
 * decrypt() function*.
 * @param {number} roundNum Round number.
 * @param {!Array<Array<number>>} Current state.
 * @private
 */
goog.crypt.Aes.prototype.testAfterMixColumns_ = goog.nullFunction;


/**
 * Tests can populate this with a callback, and that callback will get called
 * each round right after the AddRoundKey step gets executed  encrypt().
 * @param {number} roundNum Round number.
 * @param {!Array<Array<number>>} Current state.
 * @private
 */
goog.crypt.Aes.prototype.testAfterAddRoundKey_ = goog.nullFunction;


/**
 * Tests can populate this with a callback, and that callback will get called
 * before each round on the round key.  *Gets called in both the encrypt() and
 * decrypt() functions.*
 * @param {number} roundNum Round number.
 * @param {Array<!Array<number>>} Computed key schedule.
 * @param {number} index The index into the key schedule to test. This is not
 *     necessarily roundNum because the key schedule is used in reverse
 *     in the case of decryption.
 * @private
 */
goog.crypt.Aes.prototype.testKeySchedule_ = goog.nullFunction;


/**
 * Helper to copy input into the AES state matrix.
 * @param {!Array<number>|!Uint8Array} input Byte array to copy into the state
 *     matrix.
 * @private
 */
goog.crypt.Aes.prototype.copyInput_ = function(input) {
  var v, p;

  goog.asserts.assert(
      input.length == goog.crypt.Aes.BLOCK_SIZE_ * 4,
      'Expecting input of 4 times block size.');

  for (var r = 0; r < goog.crypt.Aes.BLOCK_SIZE_; r++) {
    for (var c = 0; c < 4; c++) {
      p = c * 4 + r;
      v = input[p];

      goog.asserts.assert(
          v <= 255 && v >= 0,
          'Invalid input. Value %s at position %s is not a byte.', v, p);

      this.state_[r][c] = v;
    }
  }
};


/**
 * Helper to copy the state matrix into an output array.
 * @return {!Array<number>} Output byte array.
 * @private
 */
goog.crypt.Aes.prototype.generateOutput_ = function() {
  var output = [];
  for (var r = 0; r < goog.crypt.Aes.BLOCK_SIZE_; r++) {
    for (var c = 0; c < 4; c++) {
      output[c * 4 + r] = this.state_[r][c];
    }
  }
  return output;
};


/**
 * AES's AddRoundKey procedure. Add the current round key to the state.
 * @param {number} round The current round.
 * @private
 */
goog.crypt.Aes.prototype.addRoundKey_ = function(round) {
  for (var r = 0; r < 4; r++) {
    for (var c = 0; c < 4; c++) {
      this.state_[r][c] ^= this.keySchedule_[round * 4 + c][r];
    }
  }
};


/**
 * AES's SubBytes procedure. Substitute bytes from the precomputed SBox lookup
 * into the state.
 * @param {!Array<number>} box The SBox or invSBox.
 * @private
 */
goog.crypt.Aes.prototype.subBytes_ = function(box) {
  for (var r = 0; r < 4; r++) {
    for (var c = 0; c < 4; c++) {
      this.state_[r][c] = box[this.state_[r][c]];
    }
  }
};


/**
 * AES's ShiftRows procedure. Shift the values in each row to the right. Each
 * row is shifted one more slot than the one above it.
 * @private
 */
goog.crypt.Aes.prototype.shiftRows_ = function() {
  for (var r = 1; r < 4; r++) {
    for (var c = 0; c < 4; c++) {
      this.temp_[r][c] = this.state_[r][c];
    }
  }

  for (var r = 1; r < 4; r++) {
    for (var c = 0; c < 4; c++) {
      this.state_[r][c] = this.temp_[r][(c + r) % goog.crypt.Aes.BLOCK_SIZE_];
    }
  }
};


/**
 * AES's InvShiftRows procedure. Shift the values in each row to the right.
 * @private
 */
goog.crypt.Aes.prototype.invShiftRows_ = function() {
  for (var r = 1; r < 4; r++) {
    for (var c = 0; c < 4; c++) {
      this.temp_[r][(c + r) % goog.crypt.Aes.BLOCK_SIZE_] = this.state_[r][c];
    }
  }

  for (var r = 1; r < 4; r++) {
    for (var c = 0; c < 4; c++) {
      this.state_[r][c] = this.temp_[r][c];
    }
  }
};


/**
 * AES's MixColumns procedure. Mix the columns of the state using magic.
 * @private
 */
goog.crypt.Aes.prototype.mixColumns_ = function() {
  var s = this.state_;
  var t = this.temp_[0];

  for (var c = 0; c < 4; c++) {
    t[0] = s[0][c];
    t[1] = s[1][c];
    t[2] = s[2][c];
    t[3] = s[3][c];

    s[0][c] =
        (goog.crypt.Aes.MULT_2_[t[0]] ^ goog.crypt.Aes.MULT_3_[t[1]] ^ t[2] ^
         t[3]);
    s[1][c] =
        (t[0] ^ goog.crypt.Aes.MULT_2_[t[1]] ^ goog.crypt.Aes.MULT_3_[t[2]] ^
         t[3]);
    s[2][c] =
        (t[0] ^ t[1] ^ goog.crypt.Aes.MULT_2_[t[2]] ^
         goog.crypt.Aes.MULT_3_[t[3]]);
    s[3][c] =
        (goog.crypt.Aes.MULT_3_[t[0]] ^ t[1] ^ t[2] ^
         goog.crypt.Aes.MULT_2_[t[3]]);
  }
};


/**
 * AES's InvMixColumns procedure.
 * @private
 */
goog.crypt.Aes.prototype.invMixColumns_ = function() {
  var s = this.state_;
  var t = this.temp_[0];

  for (var c = 0; c < 4; c++) {
    t[0] = s[0][c];
    t[1] = s[1][c];
    t[2] = s[2][c];
    t[3] = s[3][c];

    s[0][c] =
        (goog.crypt.Aes.MULT_E_[t[0]] ^ goog.crypt.Aes.MULT_B_[t[1]] ^
         goog.crypt.Aes.MULT_D_[t[2]] ^ goog.crypt.Aes.MULT_9_[t[3]]);

    s[1][c] =
        (goog.crypt.Aes.MULT_9_[t[0]] ^ goog.crypt.Aes.MULT_E_[t[1]] ^
         goog.crypt.Aes.MULT_B_[t[2]] ^ goog.crypt.Aes.MULT_D_[t[3]]);

    s[2][c] =
        (goog.crypt.Aes.MULT_D_[t[0]] ^ goog.crypt.Aes.MULT_9_[t[1]] ^
         goog.crypt.Aes.MULT_E_[t[2]] ^ goog.crypt.Aes.MULT_B_[t[3]]);

    s[3][c] =
        (goog.crypt.Aes.MULT_B_[t[0]] ^ goog.crypt.Aes.MULT_D_[t[1]] ^
         goog.crypt.Aes.MULT_9_[t[2]] ^ goog.crypt.Aes.MULT_E_[t[3]]);
  }
};


/**
 * AES's KeyExpansion procedure. Create the key schedule from the initial key.
 * @private
 */
goog.crypt.Aes.prototype.keyExpansion_ = function() {
  this.keySchedule_ =
      new Array(goog.crypt.Aes.BLOCK_SIZE_ * (this.numberOfRounds_ + 1));

  for (var rowNum = 0; rowNum < this.keyLength_; rowNum++) {
    this.keySchedule_[rowNum] = [
      this.key_[4 * rowNum], this.key_[4 * rowNum + 1],
      this.key_[4 * rowNum + 2], this.key_[4 * rowNum + 3]
    ];
  }

  var temp = new Array(4);

  for (var rowNum = this.keyLength_;
       rowNum < (goog.crypt.Aes.BLOCK_SIZE_ * (this.numberOfRounds_ + 1));
       rowNum++) {
    temp[0] = this.keySchedule_[rowNum - 1][0];
    temp[1] = this.keySchedule_[rowNum - 1][1];
    temp[2] = this.keySchedule_[rowNum - 1][2];
    temp[3] = this.keySchedule_[rowNum - 1][3];

    if (rowNum % this.keyLength_ == 0) {
      this.rotWord_(temp);
      this.subWord_(temp);

      temp[0] ^= goog.crypt.Aes.RCON_[rowNum / this.keyLength_][0];
      temp[1] ^= goog.crypt.Aes.RCON_[rowNum / this.keyLength_][1];
      temp[2] ^= goog.crypt.Aes.RCON_[rowNum / this.keyLength_][2];
      temp[3] ^= goog.crypt.Aes.RCON_[rowNum / this.keyLength_][3];
    } else if (this.keyLength_ > 6 && rowNum % this.keyLength_ == 4) {
      this.subWord_(temp);
    }

    this.keySchedule_[rowNum] = new Array(4);
    this.keySchedule_[rowNum][0] =
        this.keySchedule_[rowNum - this.keyLength_][0] ^ temp[0];
    this.keySchedule_[rowNum][1] =
        this.keySchedule_[rowNum - this.keyLength_][1] ^ temp[1];
    this.keySchedule_[rowNum][2] =
        this.keySchedule_[rowNum - this.keyLength_][2] ^ temp[2];
    this.keySchedule_[rowNum][3] =
        this.keySchedule_[rowNum - this.keyLength_][3] ^ temp[3];
  }
};


/**
 * AES's SubWord procedure.
 * @param {!Array<number>} w Bytes to find the SBox substitution for.
 * @return {!Array<number>} The substituted bytes.
 * @private
 */
goog.crypt.Aes.prototype.subWord_ = function(w) {
  w[0] = goog.crypt.Aes.SBOX_[w[0]];
  w[1] = goog.crypt.Aes.SBOX_[w[1]];
  w[2] = goog.crypt.Aes.SBOX_[w[2]];
  w[3] = goog.crypt.Aes.SBOX_[w[3]];

  return w;
};


/**
 * AES's RotWord procedure.
 * @param {!Array<number>} w Array of bytes to rotate.
 * @return {!Array<number>} The rotated bytes.
 * @private
 */
goog.crypt.Aes.prototype.rotWord_ = function(w) {
  var temp = w[0];

  w[0] = w[1];
  w[1] = w[2];
  w[2] = w[3];
  w[3] = temp;

  return w;
};

// clang-format off
/**
 * Precomputed SBox lookup.
 * @type {!Array<number>}
 * @private
 */
goog.crypt.Aes.SBOX_ = [
  0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe,
  0xd7, 0xab, 0x76,

  0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c,
  0xa4, 0x72, 0xc0,

  0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71,
  0xd8, 0x31, 0x15,

  0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb,
  0x27, 0xb2, 0x75,

  0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29,
  0xe3, 0x2f, 0x84,

  0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a,
  0x4c, 0x58, 0xcf,

  0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50,
  0x3c, 0x9f, 0xa8,

  0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10,
  0xff, 0xf3, 0xd2,

  0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64,
  0x5d, 0x19, 0x73,

  0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde,
  0x5e, 0x0b, 0xdb,

  0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91,
  0x95, 0xe4, 0x79,

  0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65,
  0x7a, 0xae, 0x08,

  0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b,
  0xbd, 0x8b, 0x8a,

  0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86,
  0xc1, 0x1d, 0x9e,

  0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce,
  0x55, 0x28, 0xdf,

  0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0,
  0x54, 0xbb, 0x16
];


/**
 * Precomputed InvSBox lookup.
 * @type {!Array<number>}
 * @private
 */
goog.crypt.Aes.INV_SBOX_ = [
  0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e, 0x81,
  0xf3, 0xd7, 0xfb,

  0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4,
  0xde, 0xe9, 0xcb,

  0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42,
  0xfa, 0xc3, 0x4e,

  0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d,
  0x8b, 0xd1, 0x25,

  0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xcc, 0x5d,
  0x65, 0xb6, 0x92,

  0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7,
  0x8d, 0x9d, 0x84,

  0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8,
  0xb3, 0x45, 0x06,

  0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1, 0xaf, 0xbd, 0x03, 0x01,
  0x13, 0x8a, 0x6b,

  0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf, 0xce, 0xf0,
  0xb4, 0xe6, 0x73,

  0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37, 0xe8, 0x1c,
  0x75, 0xdf, 0x6e,

  0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa,
  0x18, 0xbe, 0x1b,

  0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe, 0x78,
  0xcd, 0x5a, 0xf4,

  0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27,
  0x80, 0xec, 0x5f,

  0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93,
  0xc9, 0x9c, 0xef,

  0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83,
  0x53, 0x99, 0x61,

  0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26, 0xe1, 0x69, 0x14, 0x63, 0x55,
  0x21, 0x0c, 0x7d
];


/**
 * Precomputed RCon lookup.
 * @type {!Array<number>}
 * @private
 */
goog.crypt.Aes.RCON_ = [
  [0x00, 0x00, 0x00, 0x00],
  [0x01, 0x00, 0x00, 0x00],
  [0x02, 0x00, 0x00, 0x00],
  [0x04, 0x00, 0x00, 0x00],
  [0x08, 0x00, 0x00, 0x00],
  [0x10, 0x00, 0x00, 0x00],
  [0x20, 0x00, 0x00, 0x00],
  [0x40, 0x00, 0x00, 0x00],
  [0x80, 0x00, 0x00, 0x00],
  [0x1b, 0x00, 0x00, 0x00],
  [0x36, 0x00, 0x00, 0x00]
];


/**
 * Precomputed lookup of multiplication by 2 in GF(2^8)
 * @type {!Array<number>}
 * @private
 */
goog.crypt.Aes.MULT_2_ = [
  0x00, 0x02, 0x04, 0x06, 0x08, 0x0A, 0x0C, 0x0E, 0x10, 0x12, 0x14, 0x16,
  0x18, 0x1A, 0x1C, 0x1E,

  0x20, 0x22, 0x24, 0x26, 0x28, 0x2A, 0x2C, 0x2E, 0x30, 0x32, 0x34, 0x36,
  0x38, 0x3A, 0x3C, 0x3E,

  0x40, 0x42, 0x44, 0x46, 0x48, 0x4A, 0x4C, 0x4E, 0x50, 0x52, 0x54, 0x56,
  0x58, 0x5A, 0x5C, 0x5E,

  0x60, 0x62, 0x64, 0x66, 0x68, 0x6A, 0x6C, 0x6E, 0x70, 0x72, 0x74, 0x76,
  0x78, 0x7A, 0x7C, 0x7E,

  0x80, 0x82, 0x84, 0x86, 0x88, 0x8A, 0x8C, 0x8E, 0x90, 0x92, 0x94, 0x96,
  0x98, 0x9A, 0x9C, 0x9E,

  0xA0, 0xA2, 0xA4, 0xA6, 0xA8, 0xAA, 0xAC, 0xAE, 0xB0, 0xB2, 0xB4, 0xB6,
  0xB8, 0xBA, 0xBC, 0xBE,

  0xC0, 0xC2, 0xC4, 0xC6, 0xC8, 0xCA, 0xCC, 0xCE, 0xD0, 0xD2, 0xD4, 0xD6,
  0xD8, 0xDA, 0xDC, 0xDE,

  0xE0, 0xE2, 0xE4, 0xE6, 0xE8, 0xEA, 0xEC, 0xEE, 0xF0, 0xF2, 0xF4, 0xF6,
  0xF8, 0xFA, 0xFC, 0xFE,

  0x1B, 0x19, 0x1F, 0x1D, 0x13, 0x11, 0x17, 0x15, 0x0B, 0x09, 0x0F, 0x0D,
  0x03, 0x01, 0x07, 0x05,

  0x3B, 0x39, 0x3F, 0x3D, 0x33, 0x31, 0x37, 0x35, 0x2B, 0x29, 0x2F, 0x2D,
  0x23, 0x21, 0x27, 0x25,

  0x5B, 0x59, 0x5F, 0x5D, 0x53, 0x51, 0x57, 0x55, 0x4B, 0x49, 0x4F, 0x4D,
  0x43, 0x41, 0x47, 0x45,

  0x7B, 0x79, 0x7F, 0x7D, 0x73, 0x71, 0x77, 0x75, 0x6B, 0x69, 0x6F, 0x6D,
  0x63, 0x61, 0x67, 0x65,

  0x9B, 0x99, 0x9F, 0x9D, 0x93, 0x91, 0x97, 0x95, 0x8B, 0x89, 0x8F, 0x8D,
  0x83, 0x81, 0x87, 0x85,

  0xBB, 0xB9, 0xBF, 0xBD, 0xB3, 0xB1, 0xB7, 0xB5, 0xAB, 0xA9, 0xAF, 0xAD,
  0xA3, 0xA1, 0xA7, 0xA5,

  0xDB, 0xD9, 0xDF, 0xDD, 0xD3, 0xD1, 0xD7, 0xD5, 0xCB, 0xC9, 0xCF, 0xCD,
  0xC3, 0xC1, 0xC7, 0xC5,

  0xFB, 0xF9, 0xFF, 0xFD, 0xF3, 0xF1, 0xF7, 0xF5, 0xEB, 0xE9, 0xEF, 0xED,
  0xE3, 0xE1, 0xE7, 0xE5
];


/**
 * Precomputed lookup of multiplication by 3 in GF(2^8)
 * @type {!Array<number>}
 * @private
 */
goog.crypt.Aes.MULT_3_ = [
  0x00, 0x03, 0x06, 0x05, 0x0C, 0x0F, 0x0A, 0x09, 0x18, 0x1B, 0x1E, 0x1D,
  0x14, 0x17, 0x12, 0x11,

  0x30, 0x33, 0x36, 0x35, 0x3C, 0x3F, 0x3A, 0x39, 0x28, 0x2B, 0x2E, 0x2D,
  0x24, 0x27, 0x22, 0x21,

  0x60, 0x63, 0x66, 0x65, 0x6C, 0x6F, 0x6A, 0x69, 0x78, 0x7B, 0x7E, 0x7D,
  0x74, 0x77, 0x72, 0x71,

  0x50, 0x53, 0x56, 0x55, 0x5C, 0x5F, 0x5A, 0x59, 0x48, 0x4B, 0x4E, 0x4D,
  0x44, 0x47, 0x42, 0x41,

  0xC0, 0xC3, 0xC6, 0xC5, 0xCC, 0xCF, 0xCA, 0xC9, 0xD8, 0xDB, 0xDE, 0xDD,
  0xD4, 0xD7, 0xD2, 0xD1,

  0xF0, 0xF3, 0xF6, 0xF5, 0xFC, 0xFF, 0xFA, 0xF9, 0xE8, 0xEB, 0xEE, 0xED,
  0xE4, 0xE7, 0xE2, 0xE1,

  0xA0, 0xA3, 0xA6, 0xA5, 0xAC, 0xAF, 0xAA, 0xA9, 0xB8, 0xBB, 0xBE, 0xBD,
  0xB4, 0xB7, 0xB2, 0xB1,

  0x90, 0x93, 0x96, 0x95, 0x9C, 0x9F, 0x9A, 0x99, 0x88, 0x8B, 0x8E, 0x8D,
  0x84, 0x87, 0x82, 0x81,

  0x9B, 0x98, 0x9D, 0x9E, 0x97, 0x94, 0x91, 0x92, 0x83, 0x80, 0x85, 0x86,
  0x8F, 0x8C, 0x89, 0x8A,

  0xAB, 0xA8, 0xAD, 0xAE, 0xA7, 0xA4, 0xA1, 0xA2, 0xB3, 0xB0, 0xB5, 0xB6,
  0xBF, 0xBC, 0xB9, 0xBA,

  0xFB, 0xF8, 0xFD, 0xFE, 0xF7, 0xF4, 0xF1, 0xF2, 0xE3, 0xE0, 0xE5, 0xE6,
  0xEF, 0xEC, 0xE9, 0xEA,

  0xCB, 0xC8, 0xCD, 0xCE, 0xC7, 0xC4, 0xC1, 0xC2, 0xD3, 0xD0, 0xD5, 0xD6,
  0xDF, 0xDC, 0xD9, 0xDA,

  0x5B, 0x58, 0x5D, 0x5E, 0x57, 0x54, 0x51, 0x52, 0x43, 0x40, 0x45, 0x46,
  0x4F, 0x4C, 0x49, 0x4A,

  0x6B, 0x68, 0x6D, 0x6E, 0x67, 0x64, 0x61, 0x62, 0x73, 0x70, 0x75, 0x76,
  0x7F, 0x7C, 0x79, 0x7A,

  0x3B, 0x38, 0x3D, 0x3E, 0x37, 0x34, 0x31, 0x32, 0x23, 0x20, 0x25, 0x26,
  0x2F, 0x2C, 0x29, 0x2A,

  0x0B, 0x08, 0x0D, 0x0E, 0x07, 0x04, 0x01, 0x02, 0x13, 0x10, 0x15, 0x16,
  0x1F, 0x1C, 0x19, 0x1A
];


/**
 * Precomputed lookup of multiplication by 9 in GF(2^8)
 * @type {!Array<number>}
 * @private
 */
goog.crypt.Aes.MULT_9_ = [
  0x00, 0x09, 0x12, 0x1B, 0x24, 0x2D, 0x36, 0x3F, 0x48, 0x41, 0x5A, 0x53,
  0x6C, 0x65, 0x7E, 0x77,

  0x90, 0x99, 0x82, 0x8B, 0xB4, 0xBD, 0xA6, 0xAF, 0xD8, 0xD1, 0xCA, 0xC3,
  0xFC, 0xF5, 0xEE, 0xE7,

  0x3B, 0x32, 0x29, 0x20, 0x1F, 0x16, 0x0D, 0x04, 0x73, 0x7A, 0x61, 0x68,
  0x57, 0x5E, 0x45, 0x4C,

  0xAB, 0xA2, 0xB9, 0xB0, 0x8F, 0x86, 0x9D, 0x94, 0xE3, 0xEA, 0xF1, 0xF8,
  0xC7, 0xCE, 0xD5, 0xDC,

  0x76, 0x7F, 0x64, 0x6D, 0x52, 0x5B, 0x40, 0x49, 0x3E, 0x37, 0x2C, 0x25,
  0x1A, 0x13, 0x08, 0x01,

  0xE6, 0xEF, 0xF4, 0xFD, 0xC2, 0xCB, 0xD0, 0xD9, 0xAE, 0xA7, 0xBC, 0xB5,
  0x8A, 0x83, 0x98, 0x91,

  0x4D, 0x44, 0x5F, 0x56, 0x69, 0x60, 0x7B, 0x72, 0x05, 0x0C, 0x17, 0x1E,
  0x21, 0x28, 0x33, 0x3A,

  0xDD, 0xD4, 0xCF, 0xC6, 0xF9, 0xF0, 0xEB, 0xE2, 0x95, 0x9C, 0x87, 0x8E,
  0xB1, 0xB8, 0xA3, 0xAA,

  0xEC, 0xE5, 0xFE, 0xF7, 0xC8, 0xC1, 0xDA, 0xD3, 0xA4, 0xAD, 0xB6, 0xBF,
  0x80, 0x89, 0x92, 0x9B,

  0x7C, 0x75, 0x6E, 0x67, 0x58, 0x51, 0x4A, 0x43, 0x34, 0x3D, 0x26, 0x2F,
  0x10, 0x19, 0x02, 0x0B,

  0xD7, 0xDE, 0xC5, 0xCC, 0xF3, 0xFA, 0xE1, 0xE8, 0x9F, 0x96, 0x8D, 0x84,
  0xBB, 0xB2, 0xA9, 0xA0,

  0x47, 0x4E, 0x55, 0x5C, 0x63, 0x6A, 0x71, 0x78, 0x0F, 0x06, 0x1D, 0x14,
  0x2B, 0x22, 0x39, 0x30,

  0x9A, 0x93, 0x88, 0x81, 0xBE, 0xB7, 0xAC, 0xA5, 0xD2, 0xDB, 0xC0, 0xC9,
  0xF6, 0xFF, 0xE4, 0xED,

  0x0A, 0x03, 0x18, 0x11, 0x2E, 0x27, 0x3C, 0x35, 0x42, 0x4B, 0x50, 0x59,
  0x66, 0x6F, 0x74, 0x7D,

  0xA1, 0xA8, 0xB3, 0xBA, 0x85, 0x8C, 0x97, 0x9E, 0xE9, 0xE0, 0xFB, 0xF2,
  0xCD, 0xC4, 0xDF, 0xD6,

  0x31, 0x38, 0x23, 0x2A, 0x15, 0x1C, 0x07, 0x0E, 0x79, 0x70, 0x6B, 0x62,
  0x5D, 0x54, 0x4F, 0x46
];


/**
 * Precomputed lookup of multiplication by 11 in GF(2^8)
 * @type {!Array<number>}
 * @private
 */
goog.crypt.Aes.MULT_B_ = [
  0x00, 0x0B, 0x16, 0x1D, 0x2C, 0x27, 0x3A, 0x31, 0x58, 0x53, 0x4E, 0x45,
  0x74, 0x7F, 0x62, 0x69,

  0xB0, 0xBB, 0xA6, 0xAD, 0x9C, 0x97, 0x8A, 0x81, 0xE8, 0xE3, 0xFE, 0xF5,
  0xC4, 0xCF, 0xD2, 0xD9,

  0x7B, 0x70, 0x6D, 0x66, 0x57, 0x5C, 0x41, 0x4A, 0x23, 0x28, 0x35, 0x3E,
  0x0F, 0x04, 0x19, 0x12,

  0xCB, 0xC0, 0xDD, 0xD6, 0xE7, 0xEC, 0xF1, 0xFA, 0x93, 0x98, 0x85, 0x8E,
  0xBF, 0xB4, 0xA9, 0xA2,

  0xF6, 0xFD, 0xE0, 0xEB, 0xDA, 0xD1, 0xCC, 0xC7, 0xAE, 0xA5, 0xB8, 0xB3,
  0x82, 0x89, 0x94, 0x9F,

  0x46, 0x4D, 0x50, 0x5B, 0x6A, 0x61, 0x7C, 0x77, 0x1E, 0x15, 0x08, 0x03,
  0x32, 0x39, 0x24, 0x2F,

  0x8D, 0x86, 0x9B, 0x90, 0xA1, 0xAA, 0xB7, 0xBC, 0xD5, 0xDE, 0xC3, 0xC8,
  0xF9, 0xF2, 0xEF, 0xE4,

  0x3D, 0x36, 0x2B, 0x20, 0x11, 0x1A, 0x07, 0x0C, 0x65, 0x6E, 0x73, 0x78,
  0x49, 0x42, 0x5F, 0x54,

  0xF7, 0xFC, 0xE1, 0xEA, 0xDB, 0xD0, 0xCD, 0xC6, 0xAF, 0xA4, 0xB9, 0xB2,
  0x83, 0x88, 0x95, 0x9E,

  0x47, 0x4C, 0x51, 0x5A, 0x6B, 0x60, 0x7D, 0x76, 0x1F, 0x14, 0x09, 0x02,
  0x33, 0x38, 0x25, 0x2E,

  0x8C, 0x87, 0x9A, 0x91, 0xA0, 0xAB, 0xB6, 0xBD, 0xD4, 0xDF, 0xC2, 0xC9,
  0xF8, 0xF3, 0xEE, 0xE5,

  0x3C, 0x37, 0x2A, 0x21, 0x10, 0x1B, 0x06, 0x0D, 0x64, 0x6F, 0x72, 0x79,
  0x48, 0x43, 0x5E, 0x55,

  0x01, 0x0A, 0x17, 0x1C, 0x2D, 0x26, 0x3B, 0x30, 0x59, 0x52, 0x4F, 0x44,
  0x75, 0x7E, 0x63, 0x68,

  0xB1, 0xBA, 0xA7, 0xAC, 0x9D, 0x96, 0x8B, 0x80, 0xE9, 0xE2, 0xFF, 0xF4,
  0xC5, 0xCE, 0xD3, 0xD8,

  0x7A, 0x71, 0x6C, 0x67, 0x56, 0x5D, 0x40, 0x4B, 0x22, 0x29, 0x34, 0x3F,
  0x0E, 0x05, 0x18, 0x13,

  0xCA, 0xC1, 0xDC, 0xD7, 0xE6, 0xED, 0xF0, 0xFB, 0x92, 0x99, 0x84, 0x8F,
  0xBE, 0xB5, 0xA8, 0xA3
];


/**
 * Precomputed lookup of multiplication by 13 in GF(2^8)
 * @type {!Array<number>}
 * @private
 */
goog.crypt.Aes.MULT_D_ = [
  0x00, 0x0D, 0x1A, 0x17, 0x34, 0x39, 0x2E, 0x23, 0x68, 0x65, 0x72, 0x7F,
  0x5C, 0x51, 0x46, 0x4B,

  0xD0, 0xDD, 0xCA, 0xC7, 0xE4, 0xE9, 0xFE, 0xF3, 0xB8, 0xB5, 0xA2, 0xAF,
  0x8C, 0x81, 0x96, 0x9B,

  0xBB, 0xB6, 0xA1, 0xAC, 0x8F, 0x82, 0x95, 0x98, 0xD3, 0xDE, 0xC9, 0xC4,
  0xE7, 0xEA, 0xFD, 0xF0,

  0x6B, 0x66, 0x71, 0x7C, 0x5F, 0x52, 0x45, 0x48, 0x03, 0x0E, 0x19, 0x14,
  0x37, 0x3A, 0x2D, 0x20,

  0x6D, 0x60, 0x77, 0x7A, 0x59, 0x54, 0x43, 0x4E, 0x05, 0x08, 0x1F, 0x12,
  0x31, 0x3C, 0x2B, 0x26,

  0xBD, 0xB0, 0xA7, 0xAA, 0x89, 0x84, 0x93, 0x9E, 0xD5, 0xD8, 0xCF, 0xC2,
  0xE1, 0xEC, 0xFB, 0xF6,

  0xD6, 0xDB, 0xCC, 0xC1, 0xE2, 0xEF, 0xF8, 0xF5, 0xBE, 0xB3, 0xA4, 0xA9,
  0x8A, 0x87, 0x90, 0x9D,

  0x06, 0x0B, 0x1C, 0x11, 0x32, 0x3F, 0x28, 0x25, 0x6E, 0x63, 0x74, 0x79,
  0x5A, 0x57, 0x40, 0x4D,

  0xDA, 0xD7, 0xC0, 0xCD, 0xEE, 0xE3, 0xF4, 0xF9, 0xB2, 0xBF, 0xA8, 0xA5,
  0x86, 0x8B, 0x9C, 0x91,

  0x0A, 0x07, 0x10, 0x1D, 0x3E, 0x33, 0x24, 0x29, 0x62, 0x6F, 0x78, 0x75,
  0x56, 0x5B, 0x4C, 0x41,

  0x61, 0x6C, 0x7B, 0x76, 0x55, 0x58, 0x4F, 0x42, 0x09, 0x04, 0x13, 0x1E,
  0x3D, 0x30, 0x27, 0x2A,

  0xB1, 0xBC, 0xAB, 0xA6, 0x85, 0x88, 0x9F, 0x92, 0xD9, 0xD4, 0xC3, 0xCE,
  0xED, 0xE0, 0xF7, 0xFA,

  0xB7, 0xBA, 0xAD, 0xA0, 0x83, 0x8E, 0x99, 0x94, 0xDF, 0xD2, 0xC5, 0xC8,
  0xEB, 0xE6, 0xF1, 0xFC,

  0x67, 0x6A, 0x7D, 0x70, 0x53, 0x5E, 0x49, 0x44, 0x0F, 0x02, 0x15, 0x18,
  0x3B, 0x36, 0x21, 0x2C,

  0x0C, 0x01, 0x16, 0x1B, 0x38, 0x35, 0x22, 0x2F, 0x64, 0x69, 0x7E, 0x73,
  0x50, 0x5D, 0x4A, 0x47,

  0xDC, 0xD1, 0xC6, 0xCB, 0xE8, 0xE5, 0xF2, 0xFF, 0xB4, 0xB9, 0xAE, 0xA3,
  0x80, 0x8D, 0x9A, 0x97
];


/**
 * Precomputed lookup of multiplication by 14 in GF(2^8)
 * @type {!Array<number>}
 * @private
 */
goog.crypt.Aes.MULT_E_ = [
  0x00, 0x0E, 0x1C, 0x12, 0x38, 0x36, 0x24, 0x2A, 0x70, 0x7E, 0x6C, 0x62,
  0x48, 0x46, 0x54, 0x5A,

  0xE0, 0xEE, 0xFC, 0xF2, 0xD8, 0xD6, 0xC4, 0xCA, 0x90, 0x9E, 0x8C, 0x82,
  0xA8, 0xA6, 0xB4, 0xBA,

  0xDB, 0xD5, 0xC7, 0xC9, 0xE3, 0xED, 0xFF, 0xF1, 0xAB, 0xA5, 0xB7, 0xB9,
  0x93, 0x9D, 0x8F, 0x81,

  0x3B, 0x35, 0x27, 0x29, 0x03, 0x0D, 0x1F, 0x11, 0x4B, 0x45, 0x57, 0x59,
  0x73, 0x7D, 0x6F, 0x61,

  0xAD, 0xA3, 0xB1, 0xBF, 0x95, 0x9B, 0x89, 0x87, 0xDD, 0xD3, 0xC1, 0xCF,
  0xE5, 0xEB, 0xF9, 0xF7,

  0x4D, 0x43, 0x51, 0x5F, 0x75, 0x7B, 0x69, 0x67, 0x3D, 0x33, 0x21, 0x2F,
  0x05, 0x0B, 0x19, 0x17,

  0x76, 0x78, 0x6A, 0x64, 0x4E, 0x40, 0x52, 0x5C, 0x06, 0x08, 0x1A, 0x14,
  0x3E, 0x30, 0x22, 0x2C,

  0x96, 0x98, 0x8A, 0x84, 0xAE, 0xA0, 0xB2, 0xBC, 0xE6, 0xE8, 0xFA, 0xF4,
  0xDE, 0xD0, 0xC2, 0xCC,

  0x41, 0x4F, 0x5D, 0x53, 0x79, 0x77, 0x65, 0x6B, 0x31, 0x3F, 0x2D, 0x23,
  0x09, 0x07, 0x15, 0x1B,

  0xA1, 0xAF, 0xBD, 0xB3, 0x99, 0x97, 0x85, 0x8B, 0xD1, 0xDF, 0xCD, 0xC3,
  0xE9, 0xE7, 0xF5, 0xFB,

  0x9A, 0x94, 0x86, 0x88, 0xA2, 0xAC, 0xBE, 0xB0, 0xEA, 0xE4, 0xF6, 0xF8,
  0xD2, 0xDC, 0xCE, 0xC0,

  0x7A, 0x74, 0x66, 0x68, 0x42, 0x4C, 0x5E, 0x50, 0x0A, 0x04, 0x16, 0x18,
  0x32, 0x3C, 0x2E, 0x20,

  0xEC, 0xE2, 0xF0, 0xFE, 0xD4, 0xDA, 0xC8, 0xC6, 0x9C, 0x92, 0x80, 0x8E,
  0xA4, 0xAA, 0xB8, 0xB6,

  0x0C, 0x02, 0x10, 0x1E, 0x34, 0x3A, 0x28, 0x26, 0x7C, 0x72, 0x60, 0x6E,
  0x44, 0x4A, 0x58, 0x56,

  0x37, 0x39, 0x2B, 0x25, 0x0F, 0x01, 0x13, 0x1D, 0x47, 0x49, 0x5B, 0x55,
  0x7F, 0x71, 0x63, 0x6D,

  0xD7, 0xD9, 0xCB, 0xC5, 0xEF, 0xE1, 0xF3, 0xFD, 0xA7, 0xA9, 0xBB, 0xB5,
  0x9F, 0x91, 0x83, 0x8D
];
// clang-format on
